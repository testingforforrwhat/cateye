package cateye.service.impl;

import cateye.bean.po.WatchTimes;
import cateye.service.WatchTimesService;
import cateye.util.ESResponse;
import cateye.util.ESUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 场次信息业务逻辑层实现类
 * */
@Service
public class WatchTimesServiceImpl implements WatchTimesService {

    // 依赖项
    @Resource
    private ESUtil esUtil;

    /**
     * 根据场次编号，查询场次信息
     * @param wtId 场次编号
     * @return 场次信息实体模型对象
     * */
    @Override
    public WatchTimes selectOne(String wtId) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery( "wt_id" , wtId ));
        searchSourceBuilder.query( boolQueryBuilder );
        ESResponse<WatchTimes> esResponse = esUtil.search( "watch_times" , searchSourceBuilder , WatchTimes.class );
        return esResponse.getResultCount() == 0 ? null : esResponse.getData().get(0);
    }
}
