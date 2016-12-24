package com.poolye.Task;

import com.poolye.Model.Catelog;
import com.poolye.Redis.RedisService;
import com.poolye.Repository.CatelogRepository;
import com.poolye.Service.AsyncSpider;
import com.poolye.Service.Spider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 杨小龙 on 2016/7/7.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Component
public class ContentTask {

    @Autowired
    Spider spider;
    @Autowired
    CatelogRepository catelogRepository;
    @Autowired
    AsyncSpider asyncSpider;
    @Autowired
    RedisService redisService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static int times=0;


    //定时检查数据库中没有正文的章节
    @Scheduled(fixedRate = 2000)
    public void checkContent(){
        while (times<150){
            times++;
            asyncSpider.ThreadSpider();
        }

//        System.out.println("*****检查正文:" + dateFormat.format(new Date()));
        List<Catelog> catelogList = catelogRepository.findFirst3000ByStateNot(-1);

        //先将这些数据锁定
        if(catelogList.size()>0){
            try{
                int num=catelogList.size();
                for(int i=0;i<num;i++){
                    catelogList.get(i).setState(-1);
                    redisService.addQueue("content_queue",catelogList.get(i).getUuid());
                }

                catelogRepository.save(catelogList);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
