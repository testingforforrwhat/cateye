package cateye.bean.bo;

/**
 * 影院搜索业务模型类
 * 继承自BaseSearchBo搜索业务模型超类
 * */
public class CinemaSearchBo extends BaseSearchBo{

    private Integer parentId;   // 父级行政地区编号
    private Integer brandId;    // 品牌编号
    private String chinaName;   // 行政地区名称
    private Integer specialId;  // 放映厅类型编号
    private String serviceId;   // 影院服务编号
    private String keyword;     // 搜索关键字

    // getters and setters
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getChinaName() {
        return chinaName;
    }

    public void setChinaName(String chinaName) {
        this.chinaName = chinaName;
    }

    public Integer getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Integer specialId) {
        this.specialId = specialId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
