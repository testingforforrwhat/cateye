package cateye.controller;

import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;
import cateye.service.CategoryService;
import cateye.service.CommentService;
import cateye.service.FilmService;
import cateye.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 影片模块控制器
 * */
@RestController
@RequestMapping("/film")
public class FilmController {

    // 依赖项
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private CommentService commentService;

    /**
     * 影片列表页面接口
     * @param filmSearchBo 影片搜索业务模型对象
     * @return 响应报文体
     * */
    @PostMapping
    public Object list( FilmSearchBo filmSearchBo ){
        // 封装 响应报文体
        Map<String, Object> responseBody = new HashMap<>();
        // 设置 响应报文体属性
        responseBody.put( "code" , 200 );
        responseBody.put( "message" , "OK" );
        Map<String,Object> data = new HashMap<>();
        // 载荷 系统中所有的影片类型
        data.put( "categoryList" , categoryService.listAll() );
        // 载荷 系统中所有的拍摄地
        data.put( "regionList" , regionService.listAll() );
        // 载荷 满足筛选条件、分页条件、排序条件 的 影片数据
        data.put( "filmList" , filmService.listByBo( filmSearchBo ) );
        // 载荷 影片搜索业务模型对象
        data.put( "filmSearchBo" , filmSearchBo );
        responseBody.put( "data" , data );
        // 返回响应报文体
        return responseBody;
    }

    /**
     * 影片详情页面接口
     * @param filmId 要查询的影片的编号
     * @return 响应报文体
     * */
    @GetMapping( "/{id}" )
    public Object selectOne( @PathVariable("id") Integer filmId ){
        // 封装 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        // 调用 业务逻辑层 根据编号查询影片
        Film film = filmService.selectOne( filmId );
        // 判断 该编号的影片是否存在
        if( film != null ){
            // 影片存在
            responseBody.put( "code" , 200 );
            responseBody.put( "message" , "OK" );
            Map<String,Object> data = new HashMap<>();
            data.put( "film" , film );
            data.put( "commentList" , commentService.listByFilmId( filmId ) );
            responseBody.put( "data" , data );
        }else{
            // 影片不存在
            responseBody.put( "code" , 500 );
            responseBody.put( "message" , "您访问的影片不存在" );
        }
        // 返回 响应报文体
        return responseBody;
    }

}
