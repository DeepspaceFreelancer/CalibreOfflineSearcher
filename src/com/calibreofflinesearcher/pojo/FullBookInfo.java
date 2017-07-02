package com.calibreofflinesearcher.pojo;

public class FullBookInfo {

	public final int id;
	public final String title;
	public final String author;
	public final String publishDate;
	public final String path;
	public final String tags;
	public final String description;

    public FullBookInfo(final int id, final String title, final String author, final String publishDate, final String path,
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
