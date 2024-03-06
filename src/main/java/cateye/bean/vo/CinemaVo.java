package cateye.bean.vo;

import cateye.bean.po.Cinema;

import java.util.ArrayList;
import java.util.List;

/**
 * 影院详情视图模型类
 * */
public class CinemaVo {

    // 影院实体模型对象
    private Cinema cinema;
    // 影院排片列表
    private List<CinemaFilmVo> filmList = new ArrayList<>();

    // getters and setters
    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public List<CinemaFilmVo> getFilmList() {
        return filmList;
    }

    public void setFilmList(List<CinemaFilmVo> filmList) {
        this.filmList = filmList;
    }
}
