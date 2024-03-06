package cateye.service;

import cateye.bean.po.China;
import java.util.List;

/**
 * 行政地区业务逻辑层接口
 * */
public interface ChinaService {

    /**
     * 根据父级行政地区编号，查询子级行政地区列表
     * @param parentId 父级行政地区编号
     * @return 行政地区实体模型对象的集合
     * */
    List<China> listByParentId( Integer parentId );

}
