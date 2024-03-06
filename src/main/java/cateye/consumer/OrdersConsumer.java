package cateye.consumer;

import cateye.bean.bo.OrdersAddBo;
import cateye.bean.po.*;
import cateye.bean.vo.SiteVo;
import cateye.mapper.CinemaMapper;
import cateye.mapper.FilmMapper;
import cateye.mapper.OrdersMapper;
import cateye.mapper.WatchTimesMapper;
import cateye.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 订单模块 消费者
 * */
@Component
public class OrdersConsumer {

    // 依赖项
    @Resource private OrdersMapper ordersMapper;

    @Resource private RedisUtil redisUtil;

    @Resource private WatchTimesMapper watchTimesMapper;

    @Resource private CinemaMapper cinemaMapper;

    @Resource private FilmMapper filmMapper;

    /**
     * 生成订单消费者
     * @param record 订单消息对象
     * @param ack 消息回执
     * */
    @KafkaListener( topics = "orders" , groupId = "group-01" )
    public void add( ConsumerRecord<String,String> record, Acknowledgment ack ){

        // 步骤一：从Kafka MQ的orders话题中，读取一条订单消息
        OrdersAddBo ordersAddBo = JSON.parseObject( record.key() , OrdersAddBo.class );
        String authorization = record.value();

        // 从 Redis 中 根据用户身份令牌 获取 用户实体模型对象
        UserInfo userInfo = (UserInfo)( redisUtil.get( authorization ) );

        // 步骤二：将订单数据写入MySQL数据库的orders订单表中
        // 根据场次编号，查询场次实体模型对象
        WatchTimes watchTimes = watchTimesMapper.selectById( ordersAddBo.getWtId() );
        // 根据影院编号，查询影院实体模型对象
        Cinema cinema = cinemaMapper.selectById( watchTimes.getCmaId() );
        // 根据影片编号，查询影片实体模型对象
        Film film = filmMapper.selectById( watchTimes.getFilmId() );

        // 实例化 Orders 订单实体模型对象
        Orders orders = new Orders();
        // 给 Orders订单实体模型对象 的 成员属性 赋值
        orders.setOrderNo( UUID.randomUUID().toString() );
        orders.setOrderTime( new Date() );
        orders.setOrderUserId( userInfo.getUserId() );
        orders.setOrderUserNick( userInfo.getUserNickName() );
        orders.setOrderCinemaId( cinema.getCmaId() );
        orders.setOrderCinemaName( cinema.getCmaName() );
        orders.setOrderFilmId( film.getFilmId() );
        orders.setOrderFilmName( film.getFilmName() );
        orders.setOrderWdDate( watchTimes.getWdDate() );
        orders.setOrderWtBegintime( watchTimes.getWtBegintime() );
        orders.setOrderWtEndtime( watchTimes.getWtEndtime() );
        orders.setOrderWtHalls( watchTimes.getWtHalls() );
        orders.setOrderCost(
               new BigDecimal( watchTimes.getWtCost().doubleValue() * ordersAddBo.getSiteVoList().size() )
        );
        orders.setOrderSites( ordersAddBo.getSites() );
        orders.setOrderWtId( ordersAddBo.getWtId() );
        orders.setOrderIsUse( (byte)0 );
        orders.setOrderState( (byte)1 );
        // 将 订单实体模型对象 添加到 MySQL 数据库中
        ordersMapper.insert( orders );

        // 步骤三：将订单中的所有座位 的 座位冻结信息 ，从 redis中删除
        // 遍历 订单中的每一个座位
        for( SiteVo siteVo : ordersAddBo.getSiteVoList() ){
            // 根据 当前遍历到的座位 拼接出 Redis中存放的座位冻结信息的 key
            Map<String,Object> keyPayload = new HashMap<>();
            keyPayload.put( "wtId" , ordersAddBo.getWtId() );               // 载荷 场次信息
            keyPayload.put( "site" , siteVo );                              // 载荷 座位信息
            String key = "site-frozen-" + JSON.toJSONString( keyPayload );  // 生成key字符串
            // 将 当前遍历到的座位  的 座位冻结信息 从 redis 中 删除
            redisUtil.del( key );
        }

        // 步骤四：发送回执
        ack.acknowledge();

    }

}
