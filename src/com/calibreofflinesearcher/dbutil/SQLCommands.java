package com.calibreofflinesearcher.dbutil;

public class SQLCommands {

	public static final String createNormalizedTable = "CREATE TABLE IF NOT EXISTS normalizedbooks ("
			+ "id          INTEGER PRIMARY KEY, " 
			+ "title       TEXT COLLATE NOCASE, "
			+ "author      TEXT COLLATE NOCASE, " 
			+ "pubdate     TEXT COLLATE NOCASE, "
			+ "path        TEXT NOT NULL DEFAULT '', " 
			+ "tags        TEXT COLLATE NOCASE, "
			+ "description TEXT COLLATE NOCASE " + ");";

	public static final String createNormalizedTable_na = "CREATE TABLE IF NOT EXISTS normalizedbooks_na ("
			+ "id          INTEGER PRIMARY KEY, " 
			+ "title       TEXT COLLATE NOCASE, "
			+ "author      TEXT COLLATE NOCASE, " 
			+ "pubdate     TEXT COLLATE NOCASE, "
			+ "path        TEXT NOT NULL DEFAULT '', " 
			+ "tags        TEXT COLLATE NOCASE, "
			+ "description TEXT COLLATE NOCASE " + ");";
	
	public static final String dropNormalizedTable = "DROP TABLE IF EXISTS normalizedbooks;";
	public static final String dropNormalizedTable_na = "DROP TABLE IF EXISTS normalizedbooks_na;";
	
	public static final String insertNormalizedTable = "INSERT INTO normalizedbooks(id, title, author, pubdate, path, tags, description) VALUES(?, ?, ?, ?, ?, ?, ?);";
	public static final String insertNormalizedTable_na = "INSERT INTO normalizedbooks_na(id, title, author, pubdate, path, tags, description) VALUES(?, ?, ?, ?, ?, ?, ?);";
	
	public static final String querySearchInfo = "SELECT books.id, title, author_sort, pubdate, path, Group_Concat(tags.name) as tags, text as description "
			+ "FROM books, comments, tags, books_tags_link "
			+ "WHERE books.id = comments.book and books_tags_link.book=books.id and tags.id=books_tags_link.tag "
			+ "GROUP BY books.id "
			+ "ORDER BY books.id;"; 
	
}
