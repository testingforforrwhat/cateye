package cateye.mapper;

import cateye.bean.po.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 影院品牌数据源数据访问层接口
 * */
@Repository
public interface BrandMapper extends BaseMapper<Brand> {
}
