package cateye.mapper;

import cateye.bean.po.Cinema;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 影院数据源 数据访问层 接口
 * */
@Repository
public interface CinemaMapper extends BaseMapper<Cinema> {
}
