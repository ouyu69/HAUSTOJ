package com.example.haustoj.shiro;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.haustoj.annocation.AnonApi;
import com.example.haustoj.common.CommonResult;
import com.example.haustoj.common.ResultStatus;
import com.example.haustoj.utils.JwtUtils;
import com.example.haustoj.utils.RedisUtils;
import com.example.haustoj.utils.ServiceContextUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.RequestPath;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;


import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @FileName JwtFilter
 * @Description
 * @Author ouyu
 * @Date 2025-10-26
 **/



@Component
@Slf4j(topic = "haustoj")
public class JwtFilter extends AuthenticatingFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 判断是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        httpRequest.setAttribute("org.springframework.web.util.ServletRequestPathUtils.PATH",
                RequestPath.parse(httpRequest.getRequestURI(), httpRequest.getContextPath()));

        try {
            WebApplicationContext ctx = RequestContextUtils.findWebApplicationContext(httpRequest);
            RequestMappingHandlerMapping mapping = ctx.getBean(
                    "requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            HandlerExecutionChain handler = mapping.getHandler(httpRequest);
            
            // 如果没有找到处理器，允许访问（由其他过滤器处理）
            if (handler == null) {
                return true;
            }
            
            Object handlerObj = handler.getHandler();
            // 如果处理器不是HandlerMethod类型，允许访问
            if (!(handlerObj instanceof HandlerMethod)) {
                return true;
            }
            
            HandlerMethod handlerClazz = (HandlerMethod) handlerObj;
            // 判断请求是否访问的是公共接口，如果拥有@AnonApi注解则不再走登录认证，直接访问controller对应的方法
            AnonApi anonApi = ServiceContextUtils.getAnnotation(handlerClazz.getMethod(),
                    handlerClazz.getBeanType(),
                    AnonApi.class);
            // 有AnonApi注解，说明无需登录就可以访问

            if (anonApi != null) {

                // 即使api标记了不用登录，但如果请求头携带了token，可以尝试着进行登录验证。
                String jwt = httpRequest.getHeader("Authorization");
                if (jwt != null && !"null".equals(jwt)) {
                    try {
                        Claims claim = jwtUtils.getClaimByToken(jwt);
                        if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                            // 如果已经过期，则不进行登录尝试
                            return true;
                        }
                        String userId = claim.getSubject();
                        boolean hasToken = jwtUtils.hasToken(userId);
                        // 缓存中不存在，说明了token失效，则不进行登录尝试
                        if (!hasToken) {
                            return true;
                        }
                        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
                        if (userRolesVo == null) {
                            // 尝试手动登录
                            JwtToken jwtToken = new JwtToken(jwt);
                            SecurityUtils.getSubject().login(jwtToken);
                        }
                    } catch (Exception ignored) {
                        // 即使出错，也不影响正常访问无鉴权接口
                    }
                }
                return true;
            } else {
                // 没有@AnonApi注解，需要进行认证
                return false;
            }
        } catch (Exception e) {
            // 出现异常时，默认需要认证
            return false;
        }
    }

    
    /** 
     * @param servletRequest
     * @param servletResponse
     * @return AuthenticationToken
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取 token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StrUtil.isBlank(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String token = httpRequest.getHeader("Authorization");

        if (StrUtil.isBlank(token)) {
            return this.onLoginFailure(null,
                    new AuthenticationException("缺少认证令牌"), request, response);
        } else {
            // 验证JWT token有效性
            Claims claim = jwtUtils.getClaimByToken(token);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                return this.onLoginFailure(null,
                        new AuthenticationException("登录状态已失效，请重新登录！"), request, response);
            }
            String userId = claim.getSubject();
            boolean hasToken = jwtUtils.hasToken(userId);
            if (!hasToken) {
                return this.onLoginFailure(null,
                        new AuthenticationException("登录状态已失效，请重新登录！"), request, response);
            }
            if(!redisUtils.hasKey(ShiroConstant.SHIRO_TOKEN_REFRESH + userId)){
                this.refreshToken(httpRequest, httpResponse,userId);
            }
            try {
                // 手动登录
                boolean loginResult = executeLogin(request, response);
                Subject subject = SecurityUtils.getSubject();
                log.info("认证后Subject状态 - isAuthenticated: {}, principal: {}",
                        subject.isAuthenticated(), subject.getPrincipal());

                if (!subject.isAuthenticated()) {
                    log.warn("认证失败，Subject未正确设置");
                    return false;
                }

                return loginResult;
            } catch (Exception e) {
                log.error("认证过程中发生异常", e);
                return this.onLoginFailure(null,
                        new AuthenticationException("认证失败"), request, response);
            }
        }
    }

    /**
     * 刷新Token，并更新token到前端
     *
     * @param request
     * @param userId
     * @param response
     * @return
     */
    private void refreshToken(HttpServletRequest request, HttpServletResponse response, String userId) throws IOException {
        String requestId = UUID.randomUUID().toString();
        boolean locked = redisUtils.getLock(ShiroConstant.SHIRO_TOKEN_LOCK + userId, 20, requestId);// 获取锁20s
        if (locked) {
            String newToken = jwtUtils.generateToken(userId);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Authorization", newToken); //放到信息头部
            response.setHeader("Access-Control-Expose-Headers", "Refresh-Token,Authorization,Url-Type"); //让前端可用访问
            response.setHeader("Url-Type", request.getHeader("Url-Type")); // 为了前端能区别请求来源
            response.setHeader("Refresh-Token", "true"); //告知前端需要刷新token
        }
        redisUtils.releaseLock(ShiroConstant.SHIRO_TOKEN_LOCK + userId, requestId);
    }


    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        returnErrorResponse(request, response, e, ResultStatus.ACCESS_DENIED);
        return false;
    }

    private void returnErrorResponse(ServletRequest request, ServletResponse response, Exception e, ResultStatus resultStatus) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            CommonResult<Void> result = CommonResult.errorResponse(throwable.getMessage(), resultStatus);
            String json = JSONUtil.toJsonStr(result);
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Expose-Headers", "Refresh-Token,Authorization,Url-Type"); //让前端可用访问
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Url-Type", httpRequest.getHeader("Url-Type")); // 为了前端能区别请求来源
            httpResponse.setStatus(resultStatus.getCode());
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        httpServletResponse.setHeader("Access-Control-Expose-Headers",
                "Refresh-Token,Authorization,Url-Type,Content-disposition,Content-Type"); //让前端可用访问
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}