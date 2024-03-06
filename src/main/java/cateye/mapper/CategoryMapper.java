package cateye.mapper;

import cateye.bean.po.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 影片类型数据访问层接口
 * */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {
}
