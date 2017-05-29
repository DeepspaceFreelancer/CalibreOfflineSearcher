package com.calibreofflinesearcher;

class FullBookInfo {

    final int id;
    final String title;
    final String author;
    final String publishDate;
    final String path;
    final String tags;
    final String description;

    FullBookInfo(final int id, final String title, final String author, final String publishDate, final String path,
    		final String tags, final String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.path = path;
        this.tags = tags;
        this.description = description;
    }
}
