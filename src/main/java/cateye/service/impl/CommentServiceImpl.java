package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.po.Comment;
import cateye.service.CommentService;
import cateye.util.ESResponse;
import cateye.util.ESUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * 影评业务逻辑层实现类
 * */
@Service
public class CommentServiceImpl implements CommentService {

    // 依赖项
    @Resource
    private ESUtil esUtil;

    /**
     * 根据影片编号，查询该影片的评论数据
     * 在并发访问量较小的时候，实时显示最新评论的内容、
     * 在并发访问量较大的时候。缓存显示精华评论的内容。
     * @param filmId 影片编号
     * @return 该影片的评论实体模型对象集合
     * */
    @Override
    @RedisCache( duration = 10 )
    public List<Comment> listByFilmId(Integer filmId) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must( QueryBuilders.termQuery( "film_id" , filmId ) );
        searchSourceBuilder.query( boolQueryBuilder );

        searchSourceBuilder.sort( "cmmt_createtime" , SortOrder.DESC );

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        ESResponse<Comment> esResponse = esUtil.search( "comment" , searchSourceBuilder , Comment.class );

        return esResponse.getData();
    }
}
