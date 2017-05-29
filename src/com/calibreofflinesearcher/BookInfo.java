package com.calibreofflinesearcher;

class BookInfo {

    final int id;
    final String title;
    final String author;
    final String publishDate;
    final String path;    

    BookInfo(final int id, final String title, final String author, final String publishDate, final String path) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.path = path;        
    }
}
