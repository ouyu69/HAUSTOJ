package com.example.haustoj.utils;

/**
 * @FileName Constants
 * @Description
 * @Author ouyu
 * @Date 2025-10-27
 **/
public class Constants {
    public enum Account{
        TRY_LOGIN_NUM("try-login-num:"),
        REGISTER_CODE("register-code:");
        private final String code;
        Account(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    public enum Role{
        ADMIN("admin"),
        USER("user"),
        GUEST("guest");

        private final String code;
        Role(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
    public enum UserStatus{
        NORMAL(0),
        DISABLED(1),
        DELETED(2);
        private final Integer code;
        UserStatus(Integer code) {
            this.code = code;
        }
        public Integer getCode() {
            return code;
        }
    }
    public enum Email{

        OJ_URL("OJ_URL"),
        OJ_NAME("OJ_NAME"),
        OJ_SHORT_NAME("OJ_SHORT_NAME"),
        EMAIL_FROM("EMAIL_FROM"),
        EMAIL_BACKGROUND_IMG("EMAIL_BACKGROUND_IMG"),
        REGISTER_KEY_PREFIX("register-email:"),
        CHANGE_EMAIL_KEY_PREFIX("change-email:"),
        RESET_PASSWORD_KEY_PREFIX("reset-password:"),
        RESET_EMAIL_LOCK("reset-email-lock:"),
        REGISTER_EMAIL_LOCK("register-email-lock:"),
        CHANGE_EMAIL_LOCK("change-email-lock:");
        private final String value;

        Email(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    public enum Roles{
        ADMIN("admin",0),
        USER("user",1),
        GUEST("guest",2);
        private final String name;
        private final Integer code;
        Roles(String name, Integer code) {
            this.name = name;
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public Integer getCode() {
            return code;
        }
        public static Integer getCodeByName(String name) {
            for (Roles role : Roles.values()) {
                if (role.getName().equals(name)) {
                    return role.code;
                }
            }
            return null;
        }
    }
}
