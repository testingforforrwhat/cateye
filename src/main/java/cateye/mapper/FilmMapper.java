package cateye.mapper;

import cateye.bean.po.Film;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmMapper extends BaseMapper<Film> {

    /**
     * 根据分类编号查询该分类的影片列表
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片列表
     * */
    List<Film> listByTypeId( @Param("typeId") int typeId );

    /**
     * 根据分类编号查询该分类影片的总记录数
     * @param typeId 要查询的分类编号
     * @return 满足条件的影片总记录数
     * */
    Integer countByTypeId( @Param("typeId") int typeId );

    /**
     * 根据排序条件，查询影片列表
     * @param orderColumn 排序列名
     * @return 影片列表
     * */
    List<Film> listOrderBy( @Param("orderColumn") String orderColumn );

}
