package cateye.bean.bo;

/**
 * 客户模块注册业务模型类
 * */
public class CustomerRegistBo {

    private String userLoginName;   // 登录账户名称（手机号）
    private String userLoginPass;   // 登录账户密码明文
    private String userNickName;    // 客户昵称
    private String userValidate;    // 用户填写的手机验证码

    // getters and setters
    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getUserLoginPass() {
        return userLoginPass;
    }

    public void setUserLoginPass(String userLoginPass) {
        this.userLoginPass = userLoginPass;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }
}
