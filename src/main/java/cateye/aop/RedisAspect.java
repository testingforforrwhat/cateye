package cateye.aop;

import cateye.aop.annotation.RedisCache;
import cateye.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存策略 切面类
 * */
@Component
@Aspect
public class RedisAspect {

    // 依赖项
    @Resource
    private RedisUtil redisUtil;

    //  Pointcut切入点。描述execution织入表达式。描述代理的目标方法。
    @Pointcut("execution(* cateye.service.impl.*.*(..))")
    public void pointCut(){}

    // 环绕增强方法 = 前置增强 + 后置增强 + 返回值增强 + 异常增强
    @Around( value = "@annotation(redisCache)" )
    public Object around(ProceedingJoinPoint joinPoint , RedisCache redisCache) throws Throwable {

        System.out.println( "Redis ==> 开启缓存策略！ " );

        // 步骤一：去Redis中读取缓存数据
        // Redis Key 生成规则：方法签名+实参数据
        Map<String,Object> keyMap = new HashMap<>();
        keyMap.put( "signature" , joinPoint.getSignature().toString() );
        keyMap.put( "arguments" , joinPoint.getArgs() );
        String key = JSON.toJSONString( keyMap );

        while( true ) {

            // 去Redis获取缓存数据
            System.out.println("Redis ==> 去Redis中查询缓存数据！ ");
            Object cacheData = redisUtil.get(key);

            // 步骤二：判断缓存数据是否存在
            if (cacheData != null) {
                // 2.1 缓存命中，直接返回缓存数据
                System.out.println("Redis ==> 缓存命中，直接返回缓存数据！ ");
                // ==> 缓存穿透 => 判断Redis中查询到的缓存数据是否是 "null"
                return "null".equals(cacheData) ? null : cacheData;
            }

            // 2.2 缓存未命中
            // ==> 缓存击穿 => 争夺分布式锁
            if ( redisUtil.setnx("Mutex-" + key, 5000) ) {
                // ==> 缓存击穿 => 争夺分布式锁 成功

                // 步骤三：去MySQL查询数据
                System.out.println("Redis ==> 缓存未命中，去MySQL查询数据！ ");
                // 通过joinPoint连接点，调用代理的目标方法（业务逻辑层中的核心业务方法）
                Object returnValue = joinPoint.proceed();

                // 步骤四：将MySQL中查询到的数据，生成缓存到Redis中
                System.out.println("Redis ==> 生成缓存到Redis中！ ");
                // ==> 缓存穿透 => 判断 将MySQL中查询到的数据是否为null
                if (returnValue == null) {
                    // ==> 缓存穿透 => 对null空值，依然生成缓存，生命周期较短。为了避免缓存穿透。
                    redisUtil.set(key, "null", 5);
                } else {
                    // ==> 缓存穿透 => 正常生成缓存
                    redisUtil.set(
                            key,                      // redis存储的key
                            returnValue,              // redis存储的value
                            // redis数据的生命周期（单位：秒） ==> 缓存雪崩 => 生命周期中增加随机部分，避免缓存在同一时间同时失效
                            redisCache.duration() + (int)( Math.random() * redisCache.duration() / 10 )
                    );
                }

                // 返回 代理的目标方法的返回值
                return returnValue;

            } else {
                // ==> 缓存击穿 => 争夺分布式锁 失败
                // ==> 缓存击穿 => 延时 500毫秒 => 重新判断缓存数据是否存在
                Thread.currentThread().sleep(500);
            }
        }
    }

}
