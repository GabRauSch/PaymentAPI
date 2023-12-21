package com.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/finpexxia";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "146136";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

