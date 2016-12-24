package com.poolye.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Created by 杨小龙 on 2016/3/2.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Service
public class RedisService {
    @Autowired
    public StringRedisTemplate template;

//    @Autowired
//    public RedisTemplate redisTemplate;

//    public RedisService(StringRedisTemplate template){
//        this.template = template;
//    }

    public void set(String key, String value, int time){
        BoundValueOperations<String, String> ops = this.template.boundValueOps(key);
        ops.set(value, time, TimeUnit.SECONDS);
    }

    public void set(String key, String value){
        ValueOperations<String, String> ops = this.template.opsForValue();
        ops.set(key,value);
    }

    public String get(String key){
        ValueOperations<String, String> ops = this.template.opsForValue();
        return ops.get(key);
    }


    public void channel(String channel,String value){
        this.template.convertAndSend(channel,value);

//        this.template.getConnectionFactory();
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();


//        this.template.setConnectionFactory();
    }

    public long ttl(String key){
        return this.template.getExpire(key);
    }

    public long addQueue(String key,String value){
        return this.template.opsForList().leftPush(key,value);
    }

    public String getQueue(String key){
        return this.template.opsForList().leftPop(key);
    }
}

