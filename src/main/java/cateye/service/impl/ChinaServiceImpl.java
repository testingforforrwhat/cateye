package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.China;
import cateye.mapper.ChinaMapper;
import cateye.service.ChinaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 行政地区业务逻辑层实现类
 * */
@Service
public class ChinaServiceImpl implements ChinaService {

    // 依赖项
    @Resource
    private ChinaMapper chinaMapper;

    /**
     * 根据父级行政地区编号，查询子级行政地区列表
     * @param parentId 父级行政地区编号
     * @return 行政地区实体模型对象的集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<China> listByParentId(Integer parentId) {
        LambdaQueryWrapper<China> chinaWrapper = Wrappers.lambdaQuery();
        chinaWrapper.eq( China::getPid , parentId );
        return chinaMapper.selectList( chinaWrapper );
    }
}
