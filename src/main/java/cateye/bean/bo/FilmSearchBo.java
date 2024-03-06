package cateye.bean.bo;

/**
 * 影片搜索业务模型类
 * 继承自BaseSearchBo搜索业务模型超类
 * */
public class FilmSearchBo extends BaseSearchBo{

    // 筛选条件
    private Integer categoryId;     // 影片类型编号
    private Integer regionId;       // 拍摄地编号
    private String year;            // 上映年份
    private String keyword;         // 搜索关键字

    // getters and setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
