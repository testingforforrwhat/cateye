package cateye.controller;

import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.dto.CinemaDTO;
import cateye.bean.dto.SuperDTO;
import cateye.bean.vo.CinemaVo;
import cateye.service.BrandService;
import cateye.service.ChinaService;
import cateye.service.CinemaService;
import cateye.service.SpecialHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 影院模块控制器
 * */
@RestController
@RequestMapping("/cinema")
public class CinemaController {

    // 依赖项
    @Autowired
    private BrandService brandService;

    @Autowired
    private ChinaService chinaService;

    @Autowired
    private SpecialHallService specialHallService;

    @Autowired
    private CinemaService cinemaService;

    /**
     * 影院列表页面接口
     * @param cinemaSearchBo 影院搜索业务模型对象
     * @return 响应报文体
     * */
    @PostMapping
    public Object list(CinemaSearchBo cinemaSearchBo){
        // 实例化 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        // 设置 响应报文体 参数
        responseBody.put( "code" , 200 );
        responseBody.put( "message" , "OK" );
        Map<String,Object> data = new HashMap<>();
        // 载荷 系统中的所有品牌数据
        data.put( "brandList" , brandService.listAll() );
        // 载荷 当前选择的二级行政地区的 下属 三级行政地区列表
        data.put( "chinaList" , chinaService.listByParentId( cinemaSearchBo.getParentId() ) );
        // 载荷 系统中的所有放映厅类型
        data.put( "specialHallList" , specialHallService.listAll() );
        // 载荷 满足条件的影院列表
        data.put( "cinemaList" , cinemaService.listByBo( cinemaSearchBo ) );
        // 载荷 业务模型对象 => 实现条件持久化、分页总页数
        data.put( "cinemaSearchBo" , cinemaSearchBo );
        responseBody.put( "data" , data );
        // 返回 响应报文体
        return responseBody;
    }

    @GetMapping( "/{id}" )
    public Object one( @PathVariable("id") Integer cinemaId ){
        // 实例化 响应报文体
        SuperDTO<CinemaVo> dto = new SuperDTO();
        dto.setCode( "200" );
        dto.setMessage( "OK" );
        // 调用 业务逻辑层 实现业务功能
        dto.setData( cinemaService.selectCinemaVoByCinemaId( cinemaId ) );
        // 返回 响应报文体
        return dto;
    }

}
