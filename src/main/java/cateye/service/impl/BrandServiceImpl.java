package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.Brand;
import cateye.mapper.BrandMapper;
import cateye.service.BrandService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 影院品牌业务逻辑层实现类
 * */
@Service
public class BrandServiceImpl implements BrandService {

    // 依赖项
    @Resource
    private BrandMapper brandMapper;

    /**
     * 查询系统中所有的影院品牌数据
     * @return 影院品牌实体模型对象集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<Brand> listAll() {
        return brandMapper.selectList(null);
    }
}
