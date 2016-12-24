package com.poolye.Task;

import com.poolye.Model.Book;
import com.poolye.Redis.RedisService;
import com.poolye.Repository.BookRepository;
import com.poolye.Repository.CatelogRepository;
import com.poolye.Service.Spider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Component
public class BaseTask {
    @Autowired
    Spider spider;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CatelogRepository catelogRepository;
    @Autowired
    RedisService redisService;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





    //启动定时检查新书(60秒一次)
    @Scheduled(fixedRate = 1000*600)
    public void checkBook() {
        System.out.println("检查新书:" + dateFormat.format(new Date()));
        String urls[] = new String[]{
                "http://www.23wx.com/map/2.html",
                "http://www.23wx.com/map/1.html",
                "http://www.23wx.com/map/4.html",
                "http://www.23wx.com/map/3.html",
                "http://www.23wx.com/map/6.html",
                "http://www.23wx.com/map/7.html"
        };
        for (String item : urls) {
            spider.getBook(item);
        }
    }

    //定时检查数据库中书的章节，如果有新章节，则加入数据库
    @Scheduled(fixedRate = 1000*600)
    public void checkCatelog(){
        System.out.println("*****检查新章节:" + dateFormat.format(new Date()));
        //找到未完本的书籍
        int page = 0;
        String page_string = redisService.get("page_num");
        if(page_string!=null){
            page = Integer.parseInt(page_string);
        }

        Sort s=new Sort(Sort.Direction.ASC, "uuid");
        Page<Book> pageBook = bookRepository.findByStateNot(-1,new PageRequest(page,100,s));
        List<Book> bookList = pageBook.getContent();

        for(Book item:bookList){
            spider.getCatelog(item);
        }
        if(page<(pageBook.getTotalPages()-1)){
            page++;
        }else{
            page=0;
        }

        redisService.set("page_num",String.valueOf(page));
    }

//    @Scheduled(fixedRate = 1000*3)
//    public void checkContentNull(){
//        List<Catelog> catelogList = catelogRepository.findFirst2000ByContentIsNull();
//
//        int num = catelogList.size();
//        for(int i=0;i<num;i++){
//            catelogList.get(i).setState(0);
//        }
//        catelogRepository.save(catelogList);
//
//        System.out.println("#######抓取失误矫正########"+num);
//    }
}
