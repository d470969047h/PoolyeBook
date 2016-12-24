package com.poolye.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by 杨小龙 on 2016/7/3.
 * 邮箱：1172875805@qq.com
 * 手机：15021501312
 */
@Entity
@Table(name = "Catelog", indexes = {@Index(columnList="state"), @Index(columnList="catelog_order")})
public class Catelog {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="uuid",length = 36, nullable = false)
    @GeneratedValue(generator = "uuid")   //指定生成器名称
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @Column(name="name")
    private String name;

    @Column(name="url")
    private String url;

    @Column(name="state",nullable = false)
    private int state;

    @Column(name="catelog_order",nullable = false)
    private int order;

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Type(type="text")
//    @Column(name="content")
//    private String content;

    @ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
    @JoinColumn(name="book_id",nullable=false)
//    @JsonIgnore
    private Book book;

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

//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
