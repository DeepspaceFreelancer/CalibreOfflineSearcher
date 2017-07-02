package com.calibreofflinesearcher.dbutil;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBNormalizer {

	private SQLiteDatabase sourceConnection;
	private SQLiteDatabase targetConnection;

	public void doConversion(final String sourceLocation, final String targetLocation) {
		if (QueryExecuter.getInstance().isOpen()) {
			QueryExecuter.getInstance().disconnectQueryDatabase();
		}
		connectSource(sourceLocation);
		collectInformation();
		disconnectSource();
		connectTarget(targetLocation);
		createTargetTables();
		disconnectTarget();
		QueryExecuter.getInstance().connectQueryDatabase(targetLocation);
	}

	// region Connection 2 Source
	// ---------------------------------------------------------------
	private void connectSource(final String sourceLocation) {
		sourceConnection = SQLiteDatabase.openDatabase(sourceLocation, null,
				SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	private void disconnectSource() {
		if (sourceConnection != null) {
			sourceConnection.close();
			sourceConnection = null;
		}
	}

	List<BookInfoForNormalization> books;

	private void collectInformation() {

		books = new ArrayList<>();
		Cursor cursor = null;
		try {
			cursor = sourceConnection.rawQuery(SQLCommands.querySearchInfo, null);
			while (cursor.moveToNext()) {
				final int id = cursor.getInt(0);
				final String title = cursor.getString(1);
				final String author = cursor.getString(2);
				final String publishDate = cursor.getString(3);
				final String path = cursor.getString(4);
				final String tags = cursor.getString(5);
				final String description = cursor.getString(6);
				BookInfoForNormalization bookInfo = new BookInfoForNormalization(id, title, author,
						publishDate.substring(0, 10), path, tags, description);
				books.add(bookInfo);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	// endregion Connection 2 Source
	// ---------------------------------------------------------------

	// region Target Operations
	// ------------------------------------------------------------------------------------
	private void connectTarget(final String targetLocation) {
		targetConnection = SQLiteDatabase.openOrCreateDatabase(targetLocation, null);
	}

	private long createTargetTables() {
		targetConnection.execSQL(SQLCommands.dropNormalizedTable);
		targetConnection.execSQL(SQLCommands.dropNormalizedTable_na);
		targetConnection.execSQL(SQLCommands.createNormalizedTable);
		targetConnection.execSQL(SQLCommands.createNormalizedTable_na);

		long counter = 0;
		long counter_na = 0;
		for (BookInfoForNormalization book : books) {
			counter += insertData("normalizedbooks", book.id, book.title, book.author, book.pubdate, book.path,
					book.tags, book.description);
			counter_na += insertData("normalizedbooks_na", book.id, book.title_na, book.author_na, book.pubdate,
					book.path, book.tags_na, book.description_na);
		}
		return counter + counter_na;
	}

	private long insertData(final String table, final int id, final String title, final String author,
			final String pubdate, final String path, final String tags, final String description) {

		ContentValues values = new ContentValues();
		values.put("id", id);
		values.put("title", title);
		values.put("author", author);
		values.put("pubdate", pubdate);
		values.put("path", path);
		values.put("tags", tags);
		values.put("description", description);

		return targetConnection.insert(table, null, values);
	}

	private void disconnectTarget() {
		if (targetConnection != null) {
			targetConnection.close();
			targetConnection = null;
		}
	}
	// endregion Target Operations
	// ---------------------------------------------------------------------------------

}
