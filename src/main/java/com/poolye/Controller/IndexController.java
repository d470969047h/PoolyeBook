package com.poolye.Controller;

import com.poolye.Model.Book;
import com.poolye.Model.Catelog;
import com.poolye.Redis.RedisService;
import com.poolye.Repository.BookRepository;
import com.poolye.Repository.CatelogRepository;
import com.poolye.Service.Spider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Controller
public class IndexController {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CatelogRepository catelogRepository;
    @Autowired
    RedisService redisService;
    @Autowired
    Spider spider;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "page",required = false,defaultValue = "0")int page){
        int pageLast=0;
        int pageNext=0;

        Sort s=new Sort(Sort.Direction.ASC, "name");
        Page<Book> bookPage = bookRepository.findAll(new PageRequest(page,400,s));
        List<Book> bookList =bookPage.getContent();
        int pageNum = bookPage.getTotalPages();
        model.addAttribute("bookList",bookList);
        model.addAttribute("pageNow",page);
        if(page>0){
            pageLast=page-1;
        }
        if(page<(pageNum-1)){
            pageNext=page+1;
        }
        model.addAttribute("pageLast",pageLast);
        model.addAttribute("pageNext",pageNext);
        return "index";
    }

    @RequestMapping(value = "/Book/{uuid}",method = RequestMethod.GET)
    public String catelog(@PathVariable(value = "uuid")String uuid,Model model){
        Book book = bookRepository.findByUuid(uuid);
        if(book!=null){
            Sort s=new Sort(Sort.Direction.ASC, "order");
            List<Catelog> catelogList = catelogRepository.findByBook(book,s);
            model.addAttribute("catelogList",catelogList);
            model.addAttribute("book",book);
        }
        return "catelog";
    }

    @RequestMapping(value = "/Read/{book_id}/{content_id}",method = RequestMethod.GET)
    public String read(@PathVariable(value = "book_id")String book_id, @PathVariable(value = "content_id")int order_id,Model model){
        Book book = bookRepository.findByUuid(book_id);
        if(book!=null){
            Catelog catelog = catelogRepository.findByBookAndOrder(book,order_id);
            if(catelog==null){
                return "error";
            }else{
//                String content = redisService.get("content_"+catelog.getUuid());
                String content = readFileByLines("/yoko/poolye/data/"+catelog.getBook().getUuid()+"/"+catelog.getUuid()+".tmp");
                if(content.equals("")){
                    content="该章节暂未抓取，请稍后。";
                    if(catelog.getState()==-1){
                        catelog.setState(0);
                        catelog.setBook(book);
                        catelogRepository.saveAndFlush(catelog);
                        content="该章节暂未抓取，请稍后。[Spider Error]";
                    }
                }
                model.addAttribute("catelog",catelog);
                model.addAttribute("content",content);
                model.addAttribute("book",book);

                int lastPage = 0;
                if(catelog.getOrder()>0){
                    lastPage=catelog.getOrder()-1;
                }
                Catelog catelog1 = catelogRepository.findByBookAndOrder(book,order_id+1);
                if(catelog1!=null){
                    model.addAttribute("nextPage",catelog.getOrder()+1);
                }else{
                    model.addAttribute("nextPage",catelog.getOrder());
                }
                model.addAttribute("lastPage",lastPage);

            }
        }
        return "read";
    }

    private String readFileByLines(String fileName) {
        File file = new File(fileName);
        if(!file.exists()){
            return "";
        }
        BufferedReader reader = null;
        String re = "";
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = "";

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                re = re + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return re;
    }
}
