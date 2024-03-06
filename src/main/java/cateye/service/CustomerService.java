package cateye.service;

import cateye.bean.bo.CustomerLoginBo;
import cateye.bean.bo.CustomerRegistBo;

/**
 * 客户模块业务逻辑层接口
 * */
public interface CustomerService {

    /**
     * 客户注册业务功能
     * @param customerRegistBo 客户注册业务模型对象
     * @return 注册结果。1：注册成功。-1：短信验证码校验失败。-2：手机号已注册账户
     * */
    int regist(CustomerRegistBo customerRegistBo);

    /**
     * 客户登录业务功能
     * @param customerLoginBo 客户登录业务模型对象
     * @return 签发令牌Token。null表示登录失败。
     * */
    String login(CustomerLoginBo customerLoginBo);

}
