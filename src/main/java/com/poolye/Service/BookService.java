package com.poolye.Service;

import com.poolye.Model.Book;
import com.poolye.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    //检查一本书在数据库中是否存在，如果不存在，则写入数据库
    public void checkBook(Book book2){
        Book book = bookRepository.findByName(book2.getName());
        if(book!=null){
            if(book.getState()==0){
                book2.setUuid(book.getUuid());
            }else{
                return ;
            }
        }
        try{
            bookRepository.save(book2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
