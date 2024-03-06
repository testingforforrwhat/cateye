package cateye.mapper;

import cateye.bean.po.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 订单数据源 数据访问层 接口
 * */
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
}
