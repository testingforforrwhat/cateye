package cateye.controller;

import cateye.bean.bo.OrdersAddBo;
import cateye.service.OrdersService;
import cateye.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单模块控制器
 * */
@RestController
@RequestMapping("/orders")
public class OrdersController {

    // 依赖项
    @Resource private RedisUtil redisUtil;

    @Autowired private OrdersService ordersService;

    /**
     * 生成订单接口
     * @param authorization 用户登录身份令牌
     * @param ordersAddBo 生成订单业务模型对象
     * @return 响应报文体
     * */
    @PutMapping
    public Object add(
            @RequestHeader String authorization,
            OrdersAddBo ordersAddBo
    ){
        // 实例化 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        // 认证 用户的登录身份信息
        if( redisUtil.hashKey( authorization ) ){
            // 登录身份信息认证成功
            // 调用 业务逻辑层 实现 添加订单生产者功能
            if( ordersService.add( authorization, ordersAddBo) ){
                responseBody.put( "code" , 200 );
                responseBody.put( "message" , "生成订单成功" );
            }else{
                responseBody.put( "code" , 500 );
                responseBody.put( "message" , "生成订单失败" );
            }
        }else{
            // 登录身份信息认证失败
            responseBody.put( "code" , 401 );
            responseBody.put( "message" , "Unauthorized" );
        }
        // 返回 响应报文体
        return responseBody;
    }

}
