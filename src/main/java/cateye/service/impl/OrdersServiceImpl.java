package cateye.service.impl;

import cateye.bean.bo.OrdersAddBo;
import cateye.bean.vo.SiteVo;
import cateye.service.OrdersService;
import cateye.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单模块 业务逻辑层  实现类
 * */
@Service
public class OrdersServiceImpl implements OrdersService {

    // 依赖项
    @Resource private RedisUtil redisUtil;

    @Resource private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 添加订单
     * @param authorization 用户登录身份令牌
     * @param ordersAddBo 添加订单业务模型对象
     * @return 添加订单是否成功
     * */
    @Override
    public boolean add( String authorization, OrdersAddBo ordersAddBo ) {

        // 步骤一：对订单中的每一个座位信息，进行冻结数据验证
        // 遍历 订单中的每一个座位
        // [
        //     {"site_no":"1-5-9","site_row":"5","site_colum":"9","site_state":"1"} ,
        //     {"site_no":"1-5-10","site_row":"5","site_colum":"10","site_state":"1"} ,
        //     {"site_no":"1-5-11","site_row":"5","site_colum":"11","site_state":"1"}
        // ]
        for( SiteVo siteVo : ordersAddBo.getSiteVoList() ){
            // 根据 当前遍历到的座位 拼接出 Redis中存放的座位冻结信息的 key
            Map<String,Object> keyPayload = new HashMap<>();
            keyPayload.put( "wtId" , ordersAddBo.getWtId() );   // 载荷 场次信息
            keyPayload.put( "site" , siteVo );                  // 载荷 座位信息
            String key = "site-frozen-" + JSON.toJSONString( keyPayload );  // 生成key字符串

            // 验证 当前遍历到的座位 是否在 Redis 中 存在 冻结信息
            if( ! redisUtil.hashKey( key ) || ! redisUtil.get( key ).equals( authorization ) ){
                // 当前 遍历到的座位没有冻结信息 或则 不是为当前用户冻结的
                // 座位冻结信息验证失败 ，不能生成订单
                return false;
            }
        }

        // 座位冻结信息验证成功，可以生成订单
        // 步骤二：向Kafka MQ中的orders话题，发送一条订单消息
        kafkaTemplate.send( "orders" , JSON.toJSONString( ordersAddBo ) , authorization );

        return true;
    }
}
