package com.example.haustoj.utils;

/**
 * @FileName Constants
 * @Description
 * @Author ouyu
 * @Date 2025-10-27
 **/
public class Constants {
    public enum Account{
        TRY_LOGIN_NUM("try-login-num:");
        private final String code;
        Account(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
