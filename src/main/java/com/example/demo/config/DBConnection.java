package com.example.demo.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.load();

    public static Connection getConnection() throws Exception {
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");

            return DriverManager.getConnection(url, user, password);
    }
}
