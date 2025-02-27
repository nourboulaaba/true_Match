package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  /*  private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String DATABASE = "alphatech";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Updated driver class
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;*/

    //  private  String URL = "jdbc:mysql://mysql-alphatech.alwaysdata.net:3306/alphatech_pi?useSSL=true&requireSSL=true\";\n;
    private  static String URL = "jdbc:mysql://mysql-alphatech.alwaysdata.net:3306/alphatech_pi?useSSL=true&requireSSL=true";
    private static final String USERNAME = "alphatech";
    private String username = "alphatech";
    private String pwd = "Azerty1234@";

    private static Connection connection;
    private static DBConnection instance;
    /*static {
        try {
            // Load the JDBC driver
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load MySQL JDBC driver.");
        }
    }*/

    public DBConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, username, pwd);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to establish a database connection.", e);
            }
        }
    }

    /*public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, username, pwd);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to establish a database connection.", e);
            }
        }
        return connection;
    }*/
    public static DBConnection getInstance(){
        if(instance==null){
            instance=new DBConnection();}
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}