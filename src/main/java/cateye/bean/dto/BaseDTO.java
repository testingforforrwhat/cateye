package cateye.bean.dto;

/**
 * DTO超类
 * */
public class BaseDTO {

    private int code;       // 业务代码
    private String message; // 业务消息

    // getters and setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
