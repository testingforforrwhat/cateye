package cateye.service;

import cateye.bean.po.WatchTimes;

/**
 * 场次信息业务逻辑层接口
 * */
public interface WatchTimesService {

    /**
     * 根据场次编号，查询场次信息
     * @param wtId 场次编号
     * @return 场次信息实体模型对象
     * */
    WatchTimes selectOne( String wtId );

}
