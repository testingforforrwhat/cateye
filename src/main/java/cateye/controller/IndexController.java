package cateye.controller;

import cateye.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 * */
@RestController
@RequestMapping("/index")
public class IndexController {

    // 依赖项
    @Autowired
    private FilmService filmService;

    /**
     * 首页接口
     * @return 响应报文体
     * */
    @GetMapping
    public Object index(){
        // 创建 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put( "code" , 200 );       // 载荷业务代码
        responseBody.put( "message" , "OK" );   // 载荷业务消息
        // 创建 Map 存放 业务数据
        Map<String,Object> data = new HashMap<>();
        data.put( "currentList" , filmService.selectFilmListByTypeId( 1 ) );
        data.put( "nextList" , filmService.selectFilmListByTypeId(2) );
        data.put( "classicList" , filmService.selectFilmListByTypeId(3) );
        data.put( "currentCount" , filmService.countByTypeId( 1 ) );
        data.put( "nextCount" , filmService.countByTypeId( 2 ) );
        data.put( "costList" , filmService.selectFilmListOrderBy("film_totalcost") );
        data.put( "favoriteList" , filmService.selectFilmListOrderBy( "film_favorite" ) );
        data.put( "praiseList" , filmService.selectFilmListOrderBy( "film_praise" ) );
        responseBody.put( "data" , data );      // 载荷业务数据
        return responseBody;
    }

}
