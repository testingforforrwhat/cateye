package elasticsearch;

import org.frameworkset.tran.DataStream;
import org.frameworkset.tran.db.input.es.DB2ESImportBuilder;

/**
 * 从MySQL数据库 批量导入数据到 ElasticSearch
 * */
public class ESImport {
    public static void main(String[] args) {
        DB2ESImportBuilder importBuilder = DB2ESImportBuilder.newInstance();
        // JDBC 配置
        importBuilder.setDbName("cateyes_data")
                .setDbDriver("com.mysql.cj.jdbc.Driver")
                .setDbUrl("jdbc:mysql://url:3306/cateyes_data?useUnicode=true&characterEncoding=utf8&useCursorFetch=true")
                .setDbUser("root")
                .setDbPassword("123456")
                .setUsePool(false); //是否使用连接池

        // 将查询结果导入 ElasticSearch
        importBuilder.setSql("select * from `watch_times`");

        // ElasticSearch 配置
        importBuilder
                .setIndex("watch_times")
                .setIndexType("_doc")
                .setRefreshOption(null)
                .setUseJavaName(false)
                .setBatchSize(5000)
                .setJdbcFetchSize(10000);
        // 开始导入数据
        DataStream dataStream = importBuilder.builder();
        dataStream.execute();
    }
}
