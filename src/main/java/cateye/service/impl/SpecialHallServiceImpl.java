package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.SpecialHall;
import cateye.mapper.SpecialHallMapper;
import cateye.service.SpecialHallService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 放映厅类型业务逻辑层实现类
 * */
@Service
public class SpecialHallServiceImpl implements SpecialHallService {

    // 依赖项
    @Resource
    private SpecialHallMapper specialHallMapper;

    /**
     * 查询系统中所有的放映厅类型数据
     * @return 放映厅类型实体模型对象的集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<SpecialHall> listAll() {
        return specialHallMapper.selectList( null );
    }

}
