package org.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

  public static void connect() {
    // connection string
    String url = "jdbc:sqlite:./data/chinook.db";

    try (Connection conn = DriverManager.getConnection(url)) {
      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void queryDemo() {
    String url = "jdbc:sqlite:./data/chinook.db";
    String sql = "SELECT * FROM employees";

    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        System.out.printf(
            "%-5s%-25s%n", rs.getInt("EmployeeId"), rs.getString("LastName")
            // rs.getDouble("capacity")
            );
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  public static void createDatabase() {
    String url = "jdbc:sqlite:./data/my.db";

    try (Connection conn = DriverManager.getConnection(url)) {
      if (conn != null) {
        DatabaseMetaData meta = conn.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());
        System.out.println("A new database has been created.");
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  public static void databaseAndTable() {
    // SQLite connection string
    String url = "jdbc:sqlite:./data/my.db";

    // SQL statement for creating a new table
    String sql =
        "CREATE TABLE IF NOT EXISTS warehouses ("
            + "	id INTEGER PRIMARY KEY,"
            + "	name text NOT NULL,"
            + "	capacity REAL"
            + ");";

    try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
      // create a new table
      stmt.execute(sql);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void insertValues() {

    String url = "jdbc:sqlite:./data/my.db";

    String[] names = new String[] {"Raw Materials", "Semifinished Goods", "Finished Goods"};
    int[] capacities = new int[] {3000, 4000, 5000};

    String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

    try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      for (int i = 0; i < 3; i++) {
        pstmt.setString(1, names[i]);
        pstmt.setDouble(2, capacities[i]);
        pstmt.executeUpdate();
      }

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    connect();
    queryDemo();
    createDatabase();
    databaseAndTable();
    insertValues();
  }
}
