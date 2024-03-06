package cateye.bean.vo;

import cateye.bean.po.WatchTimes;
import java.util.ArrayList;
import java.util.List;

/**
 * 影片排片日期视图模型类
 * */
public class FilmDateVo {

    // 排片日期
    private String date;
    // 排片场次列表
    private List<WatchTimes> watchTimeList = new ArrayList<>();

    // getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<WatchTimes> getWatchTimeList() {
        return watchTimeList;
    }

    public void setWatchTimeList(List<WatchTimes> watchTimeList) {
        this.watchTimeList = watchTimeList;
    }
}
