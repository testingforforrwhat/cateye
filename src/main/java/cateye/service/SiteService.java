package cateye.service;

/**
 * 座位模块 业务逻辑层 接口
 * */
public interface SiteService {

    /**
     * 添加选座
     * @param authorization 客户登录身份令牌
     * @param wtId 场次编号
     * @param site 座位信息
     * @return 添加选座是否成功
     * */
    boolean add( String authorization , String wtId , String site );

}
