package cateye.service;

import cateye.bean.po.Brand;
import java.util.List;

/**
 * 影院品牌业务逻辑层接口
 * */
public interface BrandService {

    /**
     * 查询系统中所有的影院品牌数据
     * @return 影院品牌实体模型对象集合
     * */
    List<Brand> listAll();

}
