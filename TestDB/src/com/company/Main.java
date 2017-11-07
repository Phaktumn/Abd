package com.company;

import com.company.DataBase.Client;
import com.company.DataBase.InvoiceLines;
import com.company.DataBase.Product;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.Random;

public class Main extends Thread {


    public enum Action{
        SELL,
        LIST_SOLD_ITEMS,
        TOP10,
        ORDER,
        DELIVERY
    }
    static Action action;
    static final String DATABASE_NAME = "transactions";
    static final int PORT = 5433;
    static final int DB_ENTRIES = 10;

    public void run() {
        DbConnection conn = new DbConnection();

        System.out.println("Using Default settings");
        System.out.println("Database Name: " + DATABASE_NAME + " Port: " + PORT);
        try {
            conn.Connect(DATABASE_NAME, PORT);
            conn.SetAutoCommit();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Product product = new Product(conn, "Product");
        try {
            product.Populate(DB_ENTRIES, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Client client = new Client(conn, "Client");
        try {
            client.Populate(DB_ENTRIES, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InvoiceLines lines = new InvoiceLines(conn, "invoicelines");

        int counter = 0;
        int iterations = 100;
        while (true)
        {
            action = Action.values()[new Random().nextInt(Action.values().length)];
            counter++;
            try {
                switch (action){
                    case SELL:
                        client.Sell(new Random().nextInt(DB_ENTRIES),new Random().nextInt(DB_ENTRIES), product);
                        break;
                    case LIST_SOLD_ITEMS:
                        break;
                    case TOP10:
                        lines.ListTopNMostSold(product, 10);
                        break;
                    case ORDER:
                        break;
                    case DELIVERY:
                        break;
                }
                conn.Commit();
            } catch (PSQLException e) {
                System.out.println("--Performed a Rollback action--");
                System.out.println("\tAction -> " + action.name().toLowerCase());
                try {
                    conn.connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(counter >= iterations)
                break;
        }

        try {
            conn.CloseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 10  ; i++) {
            new Main().start();
        }

        // if(args.length != 0){
         //   conn.Connect(args[0], Integer.parseInt(args[1]));
        //    if(args.length > 2){
        //        if(Integer.parseInt(args[2]) == 0)
        //        {
        //            conn.SetAutoCommit();
        //            System.out.println("Auto Commit Set to false");
        //        }
        //    }
        ///    System.out.println("Using Custom settings");
        //    conn.PrintClientInfo();
        //}
        //else {

        //...

        //}


    }
}
