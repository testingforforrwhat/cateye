package cateye.service;

import cateye.bean.bo.OrdersAddBo;

/**
 * 订单模块 业务逻辑层 接口
 * */
public interface OrdersService {

    /**
     * 添加订单
     * @param authorization 用户登录身份令牌
     * @param ordersAddBo 添加订单业务模型对象
     * @return 添加订单是否成功
     * */
    boolean add( String authorization , OrdersAddBo ordersAddBo );

}
