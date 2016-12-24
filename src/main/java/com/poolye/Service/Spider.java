package com.poolye.Service;

import com.poolye.Model.Book;
import com.poolye.Model.Catelog;
import com.poolye.Redis.RedisService;
import com.poolye.Repository.BookRepository;
import com.poolye.Repository.CatelogRepository;
import com.poolye.Util.FileIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Service
public class Spider {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    RedisService redisService;
    @Autowired
    CatelogRepository catelogRepository;

    private static int times = 0;

    //获取小说书名列表
    //传入参数为小说列表页URL
    //0,未完结。1，已完结。-1，已完结，且已经最后检查章节
    public void getBook(String Url){
//        HashMap item = new HashMap<String,String>();
//        List result = new ArrayList();
        try{
            Document doc = Jsoup.connect(Url).timeout(30000).get();
            Elements link = doc.select("div.list ul li a");
            int num = link.size();
            for(int i=1;i<num;i=i+2){
                String BookName = link.get(i).text();
                String BookUrl = link.get(i).attr("href");
                String BookClass = link.get(i).className();

                Book book = new Book();
                book.setName(BookName);
                book.setUrl(BookUrl);
                if(BookClass.equals("state")){
                    book.setState(1);
                }else{
                    book.setState(0);
                }

                bookService.checkBook(book);
            }
        }catch (Exception e){
//            e.printStackTrace();
        }
    }



    //获取一本书的目录，并把没有的章节添加进去
    @Async
    public void getCatelog(Book book){
        String book_id = book.getUuid();
        String bookUrl = book.getUrl();

        //如果是完本，则本次是最后一次检查更新
        if(book.getState()==1){
            try{
                book.setState(-1);
                bookRepository.save(book);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try{
            Document doc = Jsoup.connect(bookUrl).timeout(50000).get();
            Elements link = doc.select("table#at a");

            int num = link.size();
            int num_cache = 0;
            String num_cache_string = redisService.get("catelog_num_cache_"+book_id);
            if(num_cache_string!=null){
                num_cache = Integer.parseInt(num_cache_string);
            }

            redisService.set("catelog_num_cache_"+book_id,String.valueOf(num));

            if(num>num_cache){
                for(int i=num_cache;i<num;i++){
                    String temp_name = link.get(i).text();
                    String temp_link = link.get(i).attr("href");

                    Catelog catelog = new Catelog();
                    catelog.setState(0);
                    catelog.setName(temp_name);
                    catelog.setUrl(bookUrl+temp_link);
                    catelog.setBook(book);
                    catelog.setOrder(i);

                    catelogRepository.save(catelog);
                }
            }
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println("获取目录超时："+bookUrl);
        }
    }

    //获取章节正文
    //@Async
    public void getContent(Catelog catelog){

        String url = catelog.getUrl();
        try{
            Document doc = Jsoup.connect(url).timeout(10000).get();
            Elements link = doc.select("#contents");
            if(link.size()>0){
                Element temp = link.get(0);
                String content = temp.html();
                try{
//                        System.out.println("写文件~~~~~");
                    FileIO fileIO = new FileIO();
                    fileIO.writeContentFile(content,catelog.getBook().getUuid(),catelog.getUuid());
                }catch (Exception e){
                    catelog.setState(0);
                    catelogRepository.saveAndFlush(catelog);
                }

            }else{
//                    System.out.println("捕获不到正文"+url);
            }

        }catch (Exception e){
            catelog.setState(0);
            catelogRepository.save(catelog);
        }

    }
}
