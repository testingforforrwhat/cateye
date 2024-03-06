package cateye.mapper;

import cateye.bean.po.CinemaFilmRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 影院影片关系数据源，数据访问层接口
 * */
@Repository
public interface CinemaFilmRelMapper extends BaseMapper<CinemaFilmRel> {
}
