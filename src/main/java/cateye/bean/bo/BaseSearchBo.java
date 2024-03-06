package cateye.bean.bo;

/**
 * 搜索业务模型超类
 * */
public class BaseSearchBo {

    // 分页条件参数
    protected Integer page = 1;       // 当前显示页码 （来自于请求报文体）
    protected Integer pageSize = 10;  // 每页显示记录数（来自于请求报文体）
    protected Integer startIndex = 0; // 查询记录的起始索引（根据page和pageSize计算得到）
    protected Integer resultCount;    // 总记录数（count聚合函数查询得到）
    protected Integer pageCount;      // 总页数（根据resultCount和pageSize计算得到）

    // 排序条件
    protected String orderColumn;     // 排序列名
    protected String orderType;       // 排序方式（asc：升序，desc：降序）

    // getters and setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
        // 根据page和pageSize计算得到startIndex
        setStartIndex( ( page - 1 ) * pageSize );
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        // 根据page和pageSize计算得到startIndex
        setStartIndex( ( page - 1 ) * pageSize );
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getResultCount() {
        return resultCount;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
        // 根据resultCount和pageSize计算得到pageCount
        setPageCount(
                resultCount % pageSize == 0 ? resultCount / pageSize : resultCount / pageSize + 1
        );
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
        // 控制page的边界值
        if( page > pageCount ){
            setPage( pageCount );
        }
        if( page < 1 ){
            setPage( 1 );
        }
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
