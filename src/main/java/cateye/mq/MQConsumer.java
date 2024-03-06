package cateye.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Kafka MQ 消费者类
 * */
@Component
public class MQConsumer {

    /**
     * 消费者处理方法
     * @param record 消息对象
     * @param ack 回执对象
     * */
    @KafkaListener( topics = "test02" , groupId = "group-01" )
    public void asynchronizeConsumer( ConsumerRecord<String,String> record , Acknowledgment ack )
        throws InterruptedException{
        System.out.println( "==> 异步业务 ==> 消费者 ==> 接收到了一条消息，key = " + record.key() + " ，value = " + record.value() );
        // 模拟耗时任务
        for( int i = 1 ; i <= 10 ; i++ ){
            System.out.println( "==> 异步业务 ==> 消费者 => 正在执行耗时任务 " + i + "/10" );
            Thread.currentThread().sleep(1000);
        }
        System.out.println( "==> 异步业务 ==> 消费者 ==> 耗时任务执行完毕" );
        // 发送回执
        ack.acknowledge();
        System.out.println( "==> 异步业务 ==> 消费者 ==> 发送回执" );
    }

}
