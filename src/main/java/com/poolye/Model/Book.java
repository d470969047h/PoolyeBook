package com.poolye.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Entity
@Table(name = "Book", indexes = {@Index(columnList="state")})
public class Book {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="uuid",length = 36, nullable = false)
    @GeneratedValue(generator = "uuid")   //指定生成器名称
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name="name",nullable = false,length = 100, unique = true)
    private String name;

    @Column(name="url")
    private String url;

    @Column(name="state",nullable = false)
    private int state;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="book")
//    @JsonIgnore
    private List<Catelog> catelogList;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Catelog> getCatelogList() {
        return catelogList;
    }

    public void setCatelogList(List<Catelog> catelogList) {
        this.catelogList = catelogList;
    }
}
