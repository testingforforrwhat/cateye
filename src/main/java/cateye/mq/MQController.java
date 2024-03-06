package cateye.mq;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * MQ消息队列中间演示案例控制器
 * */
@RestController
@RequestMapping("/mq")
public class MQController {

    /**
     * 同步业务测试接口
     * @return 响应报文体
     * */
    @GetMapping("/synchronize")
    public Object synchronize() throws InterruptedException{
        System.out.println( "==> 同步业务 => 同步方法开始执行" );
        // 模拟耗时任务
        for( int i = 1 ; i <= 10 ; i++ ){
            System.out.println( "==> 同步业务 => 正在执行耗时任务 " + i + "/10" );
            Thread.currentThread().sleep(1000);
        }
        System.out.println( "==> 同步业务 => 耗时任务执行完毕" );
        // 返回 响应报文体
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put( "code" , 200 );
        responseBody.put( "message" , "OK" );
        return responseBody;
    }

    // 依赖项
    // Kafka操作帮助类
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 异步业务测试接口
     * @return 响应报文体
     * */
    @GetMapping("/asynchronize")
    public Object asynchronize(){
        System.out.println( "==> 异步业务 ==> 生产者 ==> 开始执行" );
        // 发送消息 到 Kafka消息队列中间件 中的 test02话题Topic中
        System.out.println( "==> 异步业务 ==> 生产者 ==> 发送消息" );
        kafkaTemplate.send( "test02" , "我是消息的key" , "我是消息的value" );
        // 返回 响应报文体
        System.out.println( "==> 异步业务 ==> 生产者 ==> 响应客户端请求" );
        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put( "code" , 200 );
        responseBody.put( "message" , "OK" );
        return responseBody;
    }

}
