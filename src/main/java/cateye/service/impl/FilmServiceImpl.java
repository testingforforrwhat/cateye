package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;
import cateye.mapper.FilmMapper;
import cateye.service.FilmService;
import cateye.util.ESResponse;
import cateye.util.ESUtil;
import cateye.util.RedisUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 影片模块业务逻辑层实现类
 * */
@Service
public class FilmServiceImpl implements FilmService {

    // 依赖项
    @Resource
    private FilmMapper filmMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ESUtil esUtil;

    /**
     * 根据分类编号查询该分类的影片列表
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片列表
     * */
    @Override
    @RedisCache( duration = 60 * 5 )
    public List<Film> selectFilmListByTypeId(int typeId) {

        // 调用 数据访问层 查询满足分类条件的影片列表
        List<Film> filmList = filmMapper.listByTypeId( typeId );

        // 返回 影片列表数据
        return filmList;
    }

    /**
     * 根据分类编号查询该分类影片的总记录数
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片总记录数
     * */
    @Override
    @RedisCache( duration = 60 * 5 )
    public Integer countByTypeId(int typeId) {

        // 调用数据访问层，查询该分类的影片总记录数
        Integer resultCount = filmMapper.countByTypeId( typeId );

        // 返回总记录数
        return resultCount;
    }

    /**
     * 根据排序条件，查询影片列表
     * @param orderColumn 排序列名
     * @return 影片列表
     * */
    @Override
    @RedisCache( duration = 60 * 5 )
    public List<Film> selectFilmListOrderBy(String orderColumn) {

        // 调用数据访问层，根据指定排序列，查询影片列表
        List<Film> filmList = filmMapper.listOrderBy( orderColumn );

        // 返回影片列表
        return filmList;
    }

    /**
     * 根据 筛选条件、排序条件、分页条件，查询满足条件的影片数据
     * @param filmSearchBo 影片查询业务模型对象
     * @return 满足条件的影片实体模型对象集合
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public List<Film> listByBo(FilmSearchBo filmSearchBo) {

        // 资源搜索构建器，向ElasticSearch搜索引擎，发送查询命令的对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /////////////////////////////////////////////////////////////////////////////////////////
        // 逻辑查询构建器，构建查询条件
        // BoolQueryBuilder.must()      // 逻辑与关系连接条件
        // BoolQueryBuilder.should()    // 逻辑或关系连接条件
        // Querybuilders.termQuery()    // 不匹配分词索引，直接等性判断  film_name = '指环王'
        // Querybuilders.matchQuery()   // 匹配分词索引，执行索引匹配    film_name like '%指环王%'
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        // 判断 是否有影片类型的筛选要求
        if( filmSearchBo.getCategoryId() != null ){
            // 有 影片类型的筛选要求 => 在逻辑查询构建器中，添加影片类型的查询条件
            boolQueryBuilder.must( QueryBuilders.matchQuery( "film_cate_query" , filmSearchBo.getCategoryId() ) );
        }
        // 判断 是否有影片拍摄地的筛选要求
        if( filmSearchBo.getRegionId() != null ){
            boolQueryBuilder.must( QueryBuilders.matchQuery( "film_region_query" , filmSearchBo.getRegionId() ) );
        }
        // 判断 是否有上映年份的筛选要求
        if( filmSearchBo.getYear() != null && !filmSearchBo.getYear().isBlank() ){
            boolQueryBuilder.must( QueryBuilders.termQuery( "film_years" , filmSearchBo.getYear() ) );
        }
        // 判断 是否有搜索关键字
        if( filmSearchBo.getKeyword() != null && !filmSearchBo.getKeyword().isBlank() ){
            boolQueryBuilder.must( QueryBuilders.matchQuery( "film_name" , filmSearchBo.getKeyword() ) );
        }

        // 将 逻辑查询构建器 设置到 资源搜索构建器 中
        searchSourceBuilder.query( boolQueryBuilder );
        /////////////////////////////////////////////////////////////////////////////////////////
        // 判断 是否有排序条件
        if( filmSearchBo.getOrderColumn() != null && !filmSearchBo.getOrderColumn().isBlank() ){
            // 给 资源搜索构建器 添加排序条件
            searchSourceBuilder.sort(
                    filmSearchBo.getOrderColumn() , // 根据哪个属性进行排序
                    filmSearchBo.getOrderType() != null && filmSearchBo.getOrderType().equalsIgnoreCase("desc") ?
                            SortOrder.DESC:
                            SortOrder.ASC
            );
        }
        /////////////////////////////////////////////////////////////////////////////////////////
        // 给 资源搜索构建器 添加分页条件
        searchSourceBuilder.from( filmSearchBo.getStartIndex() );
        searchSourceBuilder.size( filmSearchBo.getPageSize() );
        /////////////////////////////////////////////////////////////////////////////////////////
        // 发送 查询命令 到 ElasticSearch搜索引擎中 进行查询
        ESResponse<Film> esResponse = esUtil.search(
                "film" ,            // 要查询的Index索引
                searchSourceBuilder ,     // 资源搜索构建器
                Film.class                // 查询到的doc文档，需要数据对象化映射成的实体模型类
        );
        // 设置满足查询条件的总记录数
        filmSearchBo.setResultCount( (int)(esResponse.getResultCount()) );
        // 返回满足条件的影片实体模型对象集合
        return esResponse.getData();
    }

    /**
     * 根据影片编号查询一部影片数据
     * 是否需要使用缓存策略？在并发访问量低的时候，不使用缓存策略，在并发访问量高的时候，启用缓存策略。Sentinel 服务降级。
     * @param filmId 要查询的影片编号
     * @return 影片实体模型对象
     * */
    @Override
    public Film selectOne(Integer filmId) {
        // 资源搜索构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 逻辑查询构建器
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must( QueryBuilders.termQuery( "film_id" , filmId ) );
        searchSourceBuilder.query( boolQueryBuilder );
        // 对ES搜索引擎 发送查询命令
        ESResponse<Film> esResponse = esUtil.search( "film" , searchSourceBuilder , Film.class );
        return esResponse.getResultCount() == 0 ? null : esResponse.getData().get(0);
    }

    /**
     * 根据影片编号，从MySQL数据库查询一部影片数据
     * @param filmId 要查询的影片编号
     * @return 影片实体模型对象
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public Film selectOneFromDB(Integer filmId) {
        return filmMapper.selectById( filmId );
    }
}
