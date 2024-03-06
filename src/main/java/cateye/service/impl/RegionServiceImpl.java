package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.FilmRegion;
import cateye.mapper.RegionMapper;
import cateye.service.RegionService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 影片拍摄地业务逻辑层实现类
 * */
@Service
public class RegionServiceImpl implements RegionService {

    // 依赖项
    @Resource
    private RegionMapper regionMapper;

    /**
     * 查询系统中所有的拍摄地
     * @return 拍摄地实体模型对象集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<FilmRegion> listAll() {
        return regionMapper.selectList( null );
    }
}
