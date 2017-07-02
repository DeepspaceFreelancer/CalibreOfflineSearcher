package com.calibreofflinesearcher.pojo;

public class BookInfo {

    public final int id;
    public final String title;
    public final String author;
    public final String publishDate;
    public final String path;    

    public BookInfo(final int id, final String title, final String author, final String publishDate, final String path) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.path = path;        
    }
}
