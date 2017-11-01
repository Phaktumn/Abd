package com.company;

import com.company.DataBase.Client;
import com.company.DataBase.Product;
import com.company.Exception.DatabaseCriticalZoneException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception {
        DbConnection conn = new DbConnection();
        conn.Connect("transactions", 5433);
        conn.SetAutoCommit();

        Product product = new Product(conn, "Product");
        product.Populate(5, true);

        Client client = new Client(conn, "Client");
        client.Populate(5, true);

        try {
            client.Sell(1, 3, product);
            //conn.Commit();
        } catch (Exception e) {
            e.printStackTrace();
            //conn.connection.rollback();
        }

        conn.CloseConnection();
    }
}
