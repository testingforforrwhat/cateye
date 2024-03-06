package cateye.service.impl;

import cateye.bean.po.Orders;
import cateye.bean.vo.SiteVo;
import cateye.service.SiteService;
import cateye.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cateye.mapper.OrdersMapper;

/**
 * 座位模块 业务逻辑层 实现类
 * */
@Service
public class SiteServiceImpl implements SiteService {

    // 依赖项
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 添加选座
     * @param authorization 客户登录身份令牌
     * @param wtId 场次编号
     * @param site 座位信息
     * @return 添加选座是否成功
     * */
    @Override
    public boolean add(String authorization, String wtId, String site) {

        // 步骤一：去Redis中判断，是否有冻结座位信息
        // 使用Map结构，载荷key信息
        Map<String,Object> keyPayload = new HashMap<>();
        // 载荷 场次信息
        keyPayload.put( "wtId" , wtId );
        // 载荷 座位信息
        keyPayload.put( "site" , JSONObject.parseObject( site , SiteVo.class ) );
        // 生成key字符串
        String key = "site-frozen-" + JSON.toJSONString( keyPayload );
        // 判断 redis中 是否存在 该key的数据
        if( redisUtil.hashKey( key ) ){
            // 该座位已经被别的客户选中了 不能添加选座
            return false;
        }

        // 步骤二：去MySQL数据库中判断，是否有该座位的订单信息
        LambdaQueryWrapper<Orders> ordersWrapper = Wrappers.lambdaQuery();
        ordersWrapper.eq( Orders::getOrderWtId , wtId );
        ordersWrapper.like( Orders::getOrderSites , "%" + site + "%" );
        List<Orders> ordersList = ordersMapper.selectList( ordersWrapper );
        if( ordersList.size() > 0 ){
            // 该座位已经被别的客户购买了 不能添加选座
            return false;
        }

        // 步骤三：争夺分布式锁
        if( ! redisUtil.setnx( "Mutrix-" + key , 5000 )  ){
            // 争夺分布式锁 失败 不能添加选座
            return false;
        }

        // 步骤四：选座成功。向Redis中添加冻结座位信息
        redisUtil.set( key , authorization , 60 * 5 );

        return true;
    }
}
