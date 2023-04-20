package com.keyin.client;

import java.sql.*;

public class DatabaseConnection {
    public Connection DBConnect(String database, String username, String password){

        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, username, password);
            if (connection != null){
                //System.out.println("Connection Successful");
            } else {
                System.out.println("Connection Failed");
                //connection.close();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error in Connecting to Server");
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        return connection;
    }

    public ResultSet queryDatabase(Connection connection,String query) {
        ResultSet rs = null;
        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
