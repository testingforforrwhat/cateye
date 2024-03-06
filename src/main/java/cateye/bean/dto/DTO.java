package cateye.bean.dto;

/**
 * 标准DTO
 * */
public class DTO extends BaseDTO{

    private Object data;        // 业务数据

    // getters and setters
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
