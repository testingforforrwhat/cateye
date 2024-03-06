package cateye.service;

import cateye.bean.po.FilmRegion;
import java.util.List;

/**
 * 影片拍摄地业务逻辑层接口
 * */
public interface RegionService {

    /**
     * 查询系统中所有的拍摄地
     * @return 拍摄地实体模型对象集合
     * */
    List<FilmRegion> listAll();

}
