package cateye.mapper;

import cateye.bean.po.FilmRegion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 影片拍摄地数据访问层接口
 * */
@Repository
public interface RegionMapper extends BaseMapper<FilmRegion> {
}
