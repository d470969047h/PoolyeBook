package com.poolye.Repository;

import com.poolye.Model.Book;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Repository
@Table(name = "Book")
@Qualifier(value = "bookRepository")
public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByUuid(String uuid);

    public Book findByName(String name);

    public Page<Book> findAll(Pageable pageable);

    public Page<Book> findByStateNot(int state, Pageable pageable);
}
