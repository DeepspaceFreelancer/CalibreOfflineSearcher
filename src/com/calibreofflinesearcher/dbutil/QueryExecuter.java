package com.calibreofflinesearcher.dbutil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.calibreofflinesearcher.pojo.BookInfo;
import com.calibreofflinesearcher.pojo.FullBookInfo;

public class QueryExecuter {

    public static class QueryControlParams {
        final boolean accentsOn;
        final boolean titleOn;
        final boolean authorOn;
        final boolean tagsOn;
        final boolean descriptionOn;

        public QueryControlParams(final boolean accentsOn, final boolean titleOn, final boolean authorOn,
                           final boolean tagsOn, final boolean descriptionOn) {
            this.accentsOn = accentsOn;
            this.titleOn = titleOn;
            this.authorOn = authorOn;
            this.tagsOn = tagsOn;
            this.descriptionOn = descriptionOn;
        }

        boolean isValid() {
            return titleOn | authorOn | tagsOn | descriptionOn;
        }
    }

    private static final QueryExecuter ourInstance = new QueryExecuter();

    public static QueryExecuter getInstance() {
        return ourInstance;
    }
    
    private QueryExecuter() {
    	connection = null;
    }


    private SQLiteDatabase connection;

    public boolean isOpen() {
        return connection != null;
    }

    //region Connection 2 Database -------------------------------------------------------------------------------------
    public void connectQueryDatabase(final String libraryLocation) {
        connection = SQLiteDatabase.openDatabase(
                libraryLocation, null,
                SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    public void disconnectQueryDatabase() {
        connection.close();
    }
    //endregion Connection 2 Database ----------------------------------------------------------------------------------

    public List<BookInfo> execute(final QueryControlParams queryControlParams, final String searchString) {
        if (!queryControlParams.isValid()) return Collections.emptyList();
        final String[] searchWords = searchString.split("\\s+");
        if (searchWords.length < 1) Collections.emptyList();

        final List<BookInfo> books = new ArrayList<>();
        final String table2Query = queryControlParams.accentsOn ? "normalizedbooks" : "normalizedbooks_na";
        String query = "SELECT * FROM " + table2Query + " WHERE ";
        List<String> whereClauses = new ArrayList<>();
        for (final String searchWord : searchWords) {
            final List<String> individualWhereClauses = new ArrayList<>();
            if (queryControlParams.titleOn) {
                individualWhereClauses.add("title LIKE '%" + searchWord + "%'");
            }
            if (queryControlParams.authorOn) {
                individualWhereClauses.add("author LIKE '%" + searchWord + "%'");
            }
            if (queryControlParams.tagsOn) {
                individualWhereClauses.add("tags LIKE '%" + searchWord + "%'");
            }
            if (queryControlParams.descriptionOn) {
                individualWhereClauses.add("description LIKE '%" + searchWord + "%'");
            }
            final String whereClause = TextUtils.join(" OR ", individualWhereClauses);
            whereClauses.add("(" + whereClause + ")");
        }
        final String where = TextUtils.join(" AND ", whereClauses);
        query += where;
        query += "ORDER BY author";

        Cursor cursor = null;
        try {
            cursor = connection.rawQuery(query, null);
            while (cursor.moveToNext()) {
                final int id = cursor.getInt(0);
                final String title = cursor.getString(1);
                final String author = cursor.getString(2);
                final String publishDate = cursor.getString(3);
                final String path = cursor.getString(4);                
                //final String tags = cursor.getString(5);
                //final String description = cursor.getString(6);
                BookInfo bookInfo = new BookInfo(id, title, author, publishDate, path);
                books.add(bookInfo);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return books;
    }
    
    public FullBookInfo getFullBookInfo(int id) {
        String query = "SELECT * FROM normalizedbooks WHERE id='" + id + "'";
        Cursor cursor = null;
        try {
            cursor = connection.rawQuery(query, null);
            while (cursor.moveToNext()) {
                final String title = cursor.getString(1);
                final String author = cursor.getString(2);
                final String publishDate = cursor.getString(3);
                final String path = cursor.getString(4);                
                final String tags = cursor.getString(5);
                final String description = cursor.getString(6);
                FullBookInfo bookInfo = new FullBookInfo(id, title, author, publishDate, path, tags, description);
                return bookInfo;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
