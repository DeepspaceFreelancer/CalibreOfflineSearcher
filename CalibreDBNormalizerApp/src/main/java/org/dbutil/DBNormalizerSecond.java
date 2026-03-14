package org.dbutil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBNormalizerSecond {

    private Connection sourceConnection;
    private Connection targetConnection;

    public void doConversion(final String sourceLocation, final String targetLocation) throws SQLException, IOException {
        connectSource(sourceLocation);
        createInMemoryTargetDatabase();
        createTargetTables();
        transformAndTransfer();
        saveTargetToDisk(targetLocation);
        disconnectSource();
        disconnectTarget();
    }

    private void saveTargetToDisk(String targetLocation) throws SQLException, IOException {
        try (FileOutputStream fos = new FileOutputStream(targetLocation);
             Statement stmt = targetConnection.createStatement()) {
            stmt.executeUpdate("backup to '" + targetLocation + "'");
        }
    }

    // region Connection 2 Source
    // ---------------------------------------------------------------
    private void connectSource(final String sourceLocation) throws SQLException {
        String connectionString = "jdbc:sqlite:file:" + sourceLocation + "?mode=ro";
        this.sourceConnection = DriverManager.getConnection(connectionString);
    }

    private void disconnectSource() throws SQLException {
        if (sourceConnection != null) {
            sourceConnection.close();
            sourceConnection = null;
        }
    }

    private void transformAndTransfer() throws SQLException {

        try (Statement stmt = this.sourceConnection.createStatement();
             ResultSet readingCursor = stmt.executeQuery(SQLCommands.querySearchInfo)) {

            while (readingCursor.next()) {
                BookInfoForNormalization book = getBookInfoForNormalization(readingCursor);
                insertData("normalizedbooks", book.id, book.title, book.author, book.pubdate, book.path,
                        book.tags, book.description);
                insertData("normalizedbooks_na", book.id, book.title_na, book.author_na, book.pubdate,
                        book.path, book.tags_na, book.description_na);
            }
        }
    }

    private static BookInfoForNormalization getBookInfoForNormalization(ResultSet cursor) throws SQLException {
        final int id = cursor.getInt(1);
        final String title = cursor.getString(2);
        final String author = cursor.getString(3);
        final String publishDate = cursor.getString(4);
        final String path = cursor.getString(5);
        final String tags = cursor.getString(6);
        final String description = cursor.getString(7);
        BookInfoForNormalization bookInfo = new BookInfoForNormalization(id, title, author,
                publishDate.substring(0, 10), path, tags, description);
        return bookInfo;
    }
    // endregion Connection 2 Source
    // ---------------------------------------------------------------

    // region Target Operations
    // ------------------------------------------------------------------------------------
    private void createInMemoryTargetDatabase() throws SQLException {
        targetConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
    }

    private void executeTargetSQL(final String sql) throws SQLException {
        try (Statement stmt = targetConnection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void createTargetTables() throws SQLException {
        this.executeTargetSQL(SQLCommands.dropNormalizedTable);
        this.executeTargetSQL(SQLCommands.dropNormalizedTable_na);
        this.executeTargetSQL(SQLCommands.createNormalizedTable);
        this.executeTargetSQL(SQLCommands.createNormalizedTable_na);
    }

    private long insertData(final String table, final int id, final String title, final String author,
                            final String pubdate, final String path, final String tags, final String description) throws SQLException {

        String sql = "INSERT INTO " + table + "(id, title, author, pubdate, path, tags, description) VALUES(?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = this.targetConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, pubdate);
            pstmt.setString(5, path);
            pstmt.setString(6, tags);
            pstmt.setString(7, description);
            return pstmt.executeUpdate();
        }
    }

    private void disconnectTarget() throws SQLException {
        if (targetConnection != null) {
            targetConnection.close();
            targetConnection = null;
        }
    }
    // endregion Target Operations
    // ---------------------------------------------------------------------------------

}
