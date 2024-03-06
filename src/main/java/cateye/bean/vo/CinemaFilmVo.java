package cateye.bean.vo;

import cateye.bean.po.Film;
import java.util.ArrayList;
import java.util.List;

/**
 * 影院中影片视图模型类
 * */
public class CinemaFilmVo {

    // 影片实体模型对象
    private Film film;
    // 影片排片日期列表
    private List<FilmDateVo> filmDateList = new ArrayList<>();

    // getters and setters
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public List<FilmDateVo> getFilmDateList() {
        return filmDateList;
    }

    public void setFilmDateList(List<FilmDateVo> filmDateList) {
        this.filmDateList = filmDateList;
    }
}
