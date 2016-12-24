package com.poolye.Service;

import com.poolye.Model.Catelog;
import com.poolye.Redis.RedisService;
import com.poolye.Repository.CatelogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by 杨小龙 on 2016/7/8.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Service
public class AsyncSpider {

    @Autowired
    RedisService redisService;
    @Autowired
    CatelogRepository catelogRepository;
    @Autowired
    Spider spider;

    private static int threadNum=0;

    @Async
    public void ThreadSpider(){
        threadNum++;
//        System.out.println(threadNum+"唤醒！！！");
        int temp = threadNum;
        while (true){
            try{
                String uuid = redisService.getQueue("content_queue");
                if(uuid!=null){
                    Catelog catelog = catelogRepository.findByUuid(uuid);
                    if(catelog!=null){
                        //System.out.println(temp+"得到："+catelog.getName());
                        spider.getContent(catelog);
                    }
                }
                Thread.sleep(300);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
