package cateye.service;

import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;
import java.util.List;

/**
 * 影片模块业务逻辑层接口
 * */
public interface FilmService {

    /**
     * 根据分类编号查询该分类的影片列表
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片列表
     * */
    List<Film> selectFilmListByTypeId( int typeId );

    /**
     * 根据分类编号查询该分类影片的总记录数
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片总记录数
     * */
    Integer countByTypeId( int typeId );

    /**
     * 根据排序条件，查询影片列表
     * @param orderColumn 排序列名
     * @return 影片列表
     * */
    List<Film> selectFilmListOrderBy( String orderColumn );

    /**
     * 根据 筛选条件、排序条件、分页条件，查询满足条件的影片数据
     * @param filmSearchBo 影片查询业务模型对象
     * @return 满足条件的影片实体模型对象集合
     * */
    List<Film> listByBo( FilmSearchBo filmSearchBo );

    /**
     * 根据影片编号,从搜索引擎查询一部影片数据
     * @param filmId 要查询的影片编号
     * @return 影片实体模型对象
     * */
    Film selectOne( Integer filmId );

    /**
     * 根据影片编号，从MySQL数据库查询一部影片数据
     * @param filmId 要查询的影片编号
     * @return 影片实体模型对象
     * */
    Film selectOneFromDB( Integer filmId );
}
