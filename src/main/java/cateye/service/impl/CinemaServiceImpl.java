package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.po.Cinema;
import cateye.bean.po.CinemaFilmRel;
import cateye.bean.po.Film;
import cateye.bean.po.WatchTimes;
import cateye.bean.vo.CinemaFilmVo;
import cateye.bean.vo.CinemaVo;
import cateye.bean.vo.FilmDateVo;
import cateye.mapper.CinemaFilmRelMapper;
import cateye.mapper.CinemaMapper;
import cateye.mapper.FilmMapper;
import cateye.mapper.WatchTimesMapper;
import cateye.service.CinemaService;
import cateye.util.ESResponse;
import cateye.util.ESUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 影院 业务逻辑层 实现类
 * */
@Service
public class CinemaServiceImpl implements CinemaService {

    // 依赖项
    @Resource
    private ESUtil esUtil;

    @Resource
    private CinemaFilmRelMapper cinemaFilmRelMapper;

    @Resource
    private FilmMapper filmMapper;

    @Resource
    private WatchTimesMapper watchTimesMapper;

    @Resource
    private CinemaMapper cinemaMapper;

    /**
     * 根据 帅选条件、分页条件、排序条件，查询满足条件的影院数据
     * @param cinemaSearchBo 影院搜索业务模型对象
     * @return 满足条件的影院实体模型对象的集合
     * */
    @Override
    @RedisCache( duration = 60 * 5 )
    public List<Cinema> listByBo(CinemaSearchBo cinemaSearchBo) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if( cinemaSearchBo.getBrandId() != null ){
            boolQueryBuilder.must(QueryBuilders.termQuery( "brand_id" , cinemaSearchBo.getBrandId() ));
        }
        if( cinemaSearchBo.getChinaName() != null && !cinemaSearchBo.getChinaName().isBlank() ){
            boolQueryBuilder.must(QueryBuilders.matchQuery( "cma_address" , cinemaSearchBo.getChinaName() ));
        }
        if( cinemaSearchBo.getSpecialId() != null ){
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_halls_query" , cinemaSearchBo.getSpecialId() ));
        }
        if( cinemaSearchBo.getServiceId() != null && !cinemaSearchBo.getServiceId().isBlank() ){
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_service_query", cinemaSearchBo.getServiceId()));
        }
        if( cinemaSearchBo.getKeyword() != null && !cinemaSearchBo.getKeyword().isBlank() ){
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_name",cinemaSearchBo.getKeyword()));
        }
        searchSourceBuilder.query( boolQueryBuilder );

        if( cinemaSearchBo.getOrderColumn() != null && !cinemaSearchBo.getOrderColumn().isBlank() ){
            searchSourceBuilder.sort(
                    cinemaSearchBo.getOrderColumn(),
                    cinemaSearchBo.getOrderType() != null && "desc".equalsIgnoreCase( cinemaSearchBo.getOrderType()) ?
                            SortOrder.DESC : SortOrder.ASC
            );
        }

        searchSourceBuilder.from( cinemaSearchBo.getStartIndex() );
        searchSourceBuilder.size( cinemaSearchBo.getPageSize() );

        ESResponse<Cinema> esResponse = esUtil.search( "cinema" , searchSourceBuilder , Cinema.class );
        cinemaSearchBo.setResultCount( (int)(esResponse.getResultCount()) );

        return esResponse.getData();
    }

    /**
     * 根据影院编号，查询影院信息
     * 并发访问量较低时：即时查询，统计影院编号的命中数
     * 并发访问量较高时：缓存策略，服务降级
     * @param cinemaId 影院编号
     * @return 影院视图模型对象
     * */
    @Override
    public CinemaVo selectCinemaVoByCinemaId(Integer cinemaId) {
        // 实例化 CinemaVo 影院视图模型对象
        CinemaVo cinemaVo = new CinemaVo();
        // 查询 影院实体模型对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery("cma_id",cinemaId));
        searchSourceBuilder.query( boolQueryBuilder );
        ESResponse<Cinema> esResponse = esUtil.search("cinema",searchSourceBuilder,Cinema.class);
        // 判断 该影院编号的影院实体模型对象 是否存在
        if( esResponse.getResultCount() == 0 ){
            // 不存在
            return null;
        }
        cinemaVo.setCinema( esResponse.getData().get(0) );
        // 查询 该影院的排片列表
        LambdaQueryWrapper<CinemaFilmRel> cinemaFilmRelWrapper = Wrappers.lambdaQuery();
        cinemaFilmRelWrapper.eq( CinemaFilmRel::getCmaId , cinemaId );
        List<CinemaFilmRel> cinemaFilmRelList = cinemaFilmRelMapper.selectList( cinemaFilmRelWrapper );
        // 循环 每一个 影院影片关系实体模型对象
        for( CinemaFilmRel cinemaFilmRel : cinemaFilmRelList ){
            // 实例化 CinemaFilmVo 影院中影片视图模型对象
            CinemaFilmVo cinemaFilmVo = new CinemaFilmVo();
            // 根据 当前循环到的影片编号 查询 影片实体模型对象
            Film film = filmMapper.selectById( cinemaFilmRel.getFilmId() );
            cinemaFilmVo.setFilm( film );
            // 根据 影院编号 和 影片编号 去 查询场次信息
            LambdaQueryWrapper<WatchTimes> watchTimesWrapper = Wrappers.lambdaQuery();
            watchTimesWrapper.eq( WatchTimes::getCmaId , cinemaId );
            watchTimesWrapper.eq( WatchTimes::getFilmId , cinemaFilmRel.getFilmId() );
            List<WatchTimes> watchTimesList = watchTimesMapper.selectList( watchTimesWrapper );
            // 准备 Set 去重 观影日期
            Set<String> dateSet = new HashSet<>();
            // 循环 每一个 排片场次 去重 排片日期
            for( WatchTimes watchTimes : watchTimesList ){
                // 将 当前遍历到的排片场次 中的 观影日期 添加到 观影日期Set中
                dateSet.add( watchTimes.getWdDate() );
            }
            // 遍历 每一个 排片日期
            for( String date : dateSet ){
                // 实例化 FilmDateVo 影片排片日期视图模型对象
                FilmDateVo filmDateVo = new FilmDateVo();
                // 设置 FilmDateVo 影片排片日期视图模型对象 排片日期
                filmDateVo.setDate( date );
                // 遍历 该影院 该影片 的 所有 场次信息
                for( WatchTimes watchTimes : watchTimesList ){
                    // 判断 当前遍历到的场次的观影日期 是否和 当前遍历到的观影日期  相同
                    if( watchTimes.getWdDate().equals( date ) ){
                        // 将 当前遍历到的场次 添加到 当前 FilmDateVo 影片排片日期视图模型对象 中的 排片场次列表
                        filmDateVo.getWatchTimeList().add( watchTimes );
                    }
                }
                // 将 FilmDateVo 影片排片日期视图模型对象 添加到 影片排片日期列表 中
                cinemaFilmVo.getFilmDateList().add( filmDateVo );
            }

            // 将  CinemaFilmVo 影院中影片视图模型对象  添加到  影院排片列表 中
            cinemaVo.getFilmList().add( cinemaFilmVo );
        }

        // 返回 CinemaVo 影院视图模型对象
        return cinemaVo;
    }

    /**
     * 根据影院编号，查询影院信息
     * @param cinemaId 影院编号
     * @return 影院实体模型对象
     * */
    @Override
    @RedisCache( duration = 60 * 60 )
    public Cinema selectOne(Integer cinemaId) {
        return cinemaMapper.selectById( cinemaId );
    }
}
