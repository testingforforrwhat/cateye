package cateye.controller;

import cateye.bean.po.WatchTimes;
import cateye.service.CinemaService;
import cateye.service.FilmService;
import cateye.service.WatchTimesService;
import cateye.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 场次信息控制器
 * */
@RestController
@RequestMapping("/watchtimes")
public class WatchTimesController {

    // 依赖项
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private WatchTimesService watchTimesService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private CinemaService cinemaService;

    /**
     * 场次信息接口（需要用户身份认证）
     * @param wtId 场次编号
     * @return 响应报文体
     * */
    @GetMapping("/{id}")
    public Object one(
            @PathVariable("id") String wtId,
            @RequestHeader String authorization ){
        // 实例化 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        // 进行 用户身份认证 => 判断 客户提交的 authorization 是否在 redis 中存在
        if( redisUtil.hashKey( authorization ) ){
            // 存在 用户身份认证 成功
            // 调用 业务逻辑层 根据场次编号查询场次信息
            WatchTimes watchTimes = watchTimesService.selectOne( wtId );
            // 判断 该场次编号的场次信息 是否存在
            if( watchTimes == null ){
                responseBody.put( "code" , 500 );
                responseBody.put( "message" , "该场次编号的场次信息不存在" );
            }else{
                responseBody.put( "code" , 200 );
                responseBody.put( "message" , "OK" );
                Map<String,Object> data = new HashMap<>();
                // 载荷 场次信息数据
                data.put( "watchTimes" , watchTimes );
                // 载荷 该场次所属的影片数据
                data.put( "film" , filmService.selectOneFromDB( watchTimes.getFilmId() ) );
                // 载荷 该场次所属的影院数据
                data.put( "cinema" , cinemaService.selectOne( watchTimes.getCmaId() ) );
                responseBody.put( "data" , data );
            }
        }else{
            // 不存在 用户身份认证 失败
            responseBody.put( "code" , 401 );
            responseBody.put( "message" , "Unauthorized" );
        }
        // 返回 响应报文体
        return responseBody;
    }

}
