package cateye.controller;

import cateye.service.SiteService;
import cateye.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 座位控制器
 * */
@RestController
@RequestMapping("/site")
public class SiteController {

    // 依赖项
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private SiteService siteService;

    /**
     * 添加选座接口
     * @param authorization 用户登录身份
     * @param wtId 场次编号
     * @param site 座位信息
     * @return 响应报文体
     * */
    @PutMapping
    public Object add(
            @RequestHeader String authorization,
            String wtId,
            String site
    ){
        // 实例化 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        // 用户登录身份验证
        if( redisUtil.hashKey( authorization ) ){
            // 用户登录身份验证成功
            // 调用业务逻辑层 执行 添加选座业务
            if( siteService.add( authorization , wtId , site ) ) {
                responseBody.put( "code" , 200 );
                responseBody.put( "message" , "选座成功" );
            }else{
                responseBody.put( "code" , 500 );
                responseBody.put( "message" , "选座失败" );
            }
        }else{
            // 用户登录身份验证失败
            responseBody.put( "code" , 401 );
            responseBody.put( "message" , "Unauthorized" );
        }
        // 返回响应报文体
        return responseBody;
    }

}
