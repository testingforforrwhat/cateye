package cateye.service;

import cateye.bean.po.SpecialHall;
import java.util.List;

/**
 * 放映厅类型业务逻辑层接口
 * */
public interface SpecialHallService {

    /**
     * 查询系统中所有的放映厅类型数据
     * @return 放映厅类型实体模型对象的集合
     * */
    List<SpecialHall> listAll();

}
