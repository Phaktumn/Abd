package com.company;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {

    protected Connection connection;

    public boolean Connect(String databaseName, int port) throws SQLException {
        String connectionString  = String.format("jdbc:postgresql://localhost:%s/%s", String.valueOf(port),databaseName);
        connection = DriverManager.getConnection(connectionString);
        return connection != null;
    }

    public Connection GetConnection() {
        return connection;
    }

    public void SetAutoCommit() throws SQLException {
        connection.setAutoCommit(!connection.getAutoCommit());
    }

    public void Commit() throws SQLException {
        //Workaround para nao me chatear com as exceptions
        if(connection.getAutoCommit() == false)
            connection.commit();
    }

    public void PrintClientInfo() throws SQLException {
        Properties properties = connection.getClientInfo();
        properties.list(System.out);
    }

    public void CloseConnection() throws SQLException {
        connection.close();
    }
}
