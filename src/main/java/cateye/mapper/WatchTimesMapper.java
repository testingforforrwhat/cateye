package cateye.mapper;

import cateye.bean.po.WatchTimes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 场次信息数据访问层接口
 * */
@Repository
public interface WatchTimesMapper extends BaseMapper<WatchTimes> {
}
