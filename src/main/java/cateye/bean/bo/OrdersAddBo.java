package cateye.bean.bo;

import cateye.bean.vo.SiteVo;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * 添加订单业务模型类
 * */
public class OrdersAddBo {

    private String wtId;                // 场次编号
    private String sites;               // 订购的座位列表字符串  "[{"site_no":"1-5-9","site_row":"5","site_colum":"9","site_state":"1"},{"site_no":"1-5-10","site_row":"5","site_colum":"10","site_state":"1"},{"site_no":"1-5-11","site_row":"5","site_colum":"11","site_state":"1"}]"
    private List<SiteVo> siteVoList;    // 订购的座位列表

    // getters and setters
    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    public String getSites() {
        return sites;
    }

    public void setSites(String sites) {
        // 订购的座位列表字符串 赋值
        this.sites = sites;
        // 将 字符串 反序列化为 订购的座位列表
        setSiteVoList( JSONArray.parseArray( sites , SiteVo.class ) );
    }

    public List<SiteVo> getSiteVoList() {
        return siteVoList;
    }

    public void setSiteVoList(List<SiteVo> siteVoList) {
        this.siteVoList = siteVoList;
    }
}
