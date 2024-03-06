package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.Category;
import cateye.mapper.CategoryMapper;
import cateye.service.CategoryService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 影片类型业务逻辑层实现类
 * */
@Service
public class CategoryServiceImpl implements CategoryService {

    // 依赖项
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 查询系统中的所有影片类型
     * @return 影片类型实体模型对象集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<Category> listAll() {
        return categoryMapper.selectList( null );
    }

}
