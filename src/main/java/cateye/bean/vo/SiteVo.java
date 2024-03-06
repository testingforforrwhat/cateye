package cateye.bean.vo;

/**
 * 座位视图模型类
 * */
public class SiteVo {

    private String site_no;     // 座位编号
    private String site_row;    // 座位行号
    private String site_colum;  // 座位列号
    private String site_state;  // 座位状态

    // setters and getters
    public String getSite_no() {
        return site_no;
    }

    public void setSite_no(String site_no) {
        this.site_no = site_no;
    }

    public String getSite_row() {
        return site_row;
    }

    public void setSite_row(String site_row) {
        this.site_row = site_row;
    }

    public String getSite_colum() {
        return site_colum;
    }

    public void setSite_colum(String site_colum) {
        this.site_colum = site_colum;
    }

    public String getSite_state() {
        return site_state;
    }

    public void setSite_state(String site_state) {
        this.site_state = site_state;
    }
}
