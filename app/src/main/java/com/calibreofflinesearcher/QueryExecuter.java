package com.calibreofflinesearcher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class QueryExecuter {

    static class QueryControlParams {
        final boolean accentsOn;
        final boolean titleOn;
        final boolean authorOn;
        final boolean tagsOn;
        final boolean descriptionOn;

        QueryControlParams(final boolean accentsOn, final boolean titleOn, final boolean authorOn,
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

    private final static String LIBRARY_NAME = "normalized.db";
    private SQLiteDatabase connection;

    //region Connection 2 Database -------------------------------------------------------------------------------------
    public void connectQueryDatabase(final String libraryLocation) {
        connection = SQLiteDatabase.openDatabase(
                libraryLocation + LIBRARY_NAME, null,
                SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    public void disconnectQueryDatabase() throws SQLException {
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
                final int id = cursor.getInt(1);
                final String title = cursor.getString(1);
                final String author = cursor.getString(2);
                final String path = cursor.getString(3);
                //final String tags = cursor.getString(5);
                //final String description = cursor.getString(6);
                BookInfo bookInfo = new BookInfo(id, title, author, path);
                books.add(bookInfo);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return books;
    }
}
