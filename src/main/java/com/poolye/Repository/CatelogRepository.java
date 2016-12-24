package com.poolye.Repository;

import com.poolye.Model.Book;
import com.poolye.Model.Catelog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
public interface CatelogRepository extends JpaRepository<Catelog, Long> {
    public Catelog findByUuid(String uuid);

    public List<Catelog> findFirst3000ByStateNot(int state);

    public List<Catelog> findByBook(Book book, Sort s);

    public Catelog findByBookAndOrder(Book book, int order);
}
