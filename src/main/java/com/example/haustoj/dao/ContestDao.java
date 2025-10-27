package com.example.haustoj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.haustoj.pojo.po.Submission;
import org.springframework.stereotype.Component;

/**
 * @Author: ouyu69
 * @Date: 2025-10-26 18:14
 * @Description:
 */
@Component
public interface ContestDao extends BaseMapper<Submission> {
}
