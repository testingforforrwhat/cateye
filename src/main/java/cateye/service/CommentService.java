package cateye.service;

import cateye.bean.po.Comment;
import java.util.List;

/**
 * 影评业务逻辑层接口
 * */
public interface CommentService {

    /**
     * 根据影片编号，查询该影片的评论数据
     * @param filmId 影片编号
     * @return 该影片的评论实体模型对象集合
     * */
    List<Comment> listByFilmId( Integer filmId );

}
