package com.company;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.Dictionary;
import java.util.Random;

public class DbConnection {

    protected Connection connection;

    public boolean Connect(String databaseName, int port) throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:"
                + String.valueOf(port) + "/" + databaseName);
        return connection != null;
    }

    public Connection GetConnection() {
        return connection;
    }

    public void CloseConnection() throws SQLException {
        connection.close();
    }
}
