package cateye.service;

import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.po.Cinema;
import cateye.bean.vo.CinemaVo;
import java.util.List;

/**
 * 影院 业务逻辑层 接口
 * */
public interface CinemaService {

    /**
     * 根据 帅选条件、分页条件、排序条件，查询满足条件的影院数据
     * @param cinemaSearchBo 影院搜索业务模型对象
     * @return 满足条件的影院实体模型对象的集合
     * */
    List<Cinema> listByBo(CinemaSearchBo cinemaSearchBo);

    /**
     * 根据影院编号，查询影院信息
     * @param cinemaId 影院编号
     * @return 影院视图模型对象
     * */
    CinemaVo selectCinemaVoByCinemaId( Integer cinemaId );

    /**
     * 根据影院编号，查询影院信息
     * @param cinemaId 影院编号
     * @return 影院实体模型对象
     * */
    Cinema selectOne( Integer cinemaId );

}
