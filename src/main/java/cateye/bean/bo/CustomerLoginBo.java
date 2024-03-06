package cateye.bean.bo;

/**
 * 客户登录业务模型类
 * */
public class CustomerLoginBo {

    private String userLoginName;       // 账户名称（手机号）
    private String userLoginPass;       // 账户密码明文

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
}
