package cateye.service;

import cateye.bean.po.Category;
import java.util.List;

/**
 * 影片类型业务逻辑层接口
 * */
public interface CategoryService {

    /**
     * 查询系统中的所有影片类型
     * @return 影片类型实体模型对象集合
     * */
    List<Category> listAll();

}
