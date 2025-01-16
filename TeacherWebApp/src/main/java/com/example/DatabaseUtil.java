package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String MYSQL_URI = "jdbc:mysql://Golearn_fastenedhe:3d89598823649489f364349f3393243c9d455c6f@ie6tw.h.filess.io:3307/Golearn_fastenedhe";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(MYSQL_URI);
    }
}
