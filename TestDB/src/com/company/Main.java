package com.company;

import com.company.DataBase.Client;
import com.company.DataBase.InvoiceLines;
import com.company.DataBase.Invoices;
import com.company.DataBase.Product;
import com.company.Utilities.ConsoleColors;
import com.company.Utilities.ConsoleColors.AnsiColor.Modifier;
import org.postgresql.util.PSQLException;
import com.company.Utilities.ColorfulConsole;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Random;

import static com.company.Utilities.ColorfulConsole.WriteLine;
import static com.company.Utilities.ConsoleColors.AnsiColor.*;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.*;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;
import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

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

    static int rollback_Count = 0;
    static int success_Count = 0;

    public void run() {
        DbConnection conn = new DbConnection();

        WriteLine(Green(Bold),"Using Default settings");
        WriteLine(Green(Bold),"Database Name: " + DATABASE_NAME + " Port: " + PORT);
        WriteLine(Green(Bold),"Connecting...");

        if(!conn.Connect(DATABASE_NAME, PORT)){
            WriteLine(Red(Bold),"[Fatal Error] Closing application");
            return;
        }

        try {
            conn.SetAutoCommit();
            if(conn.connection.getMetaData().supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE))
            {
                WriteLine(Green(Bold),"Transaction Isolation set to: Transaction Serializable");
                conn.connection.setTransactionIsolation(TRANSACTION_SERIALIZABLE);
            }
        } catch (SQLException e) {
            WriteLine(Red(Bold),"[Fatal Error] Closing application");
            return;
        }


        Product product = new Product(conn, "Product");
        Client client = new Client(conn, "Client");
        //try {
        //    product.Populate(DB_ENTRIES, true);
        //    client.Populate(DB_ENTRIES, true);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

        InvoiceLines lines = new InvoiceLines(conn, "invoiceline");
        Invoices invoices = new Invoices(conn, "invoice");
        int counter = 0;
        int iterations = 100;

        boolean rollBackState = false;
        int cr1 = 0, pr2 = 0;
        while (true)
        {
            if(counter >= iterations)
                break;
            counter++;

            if(!rollBackState) {
                action = Action.values()[new Random().nextInt(Action.values().length)];
                cr1 = 1 + new Random().nextInt(10) + client.lastInserted_ID - 10;
                pr2 = 1 + new Random().nextInt(10) + product.lastInserted_ID - 10;
            }

            try {
                switch (action){
                    case SELL:
                        client.Sell(cr1, pr2, product, lines, invoices);
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
                try {
                    //fazer o rollback
                    conn.connection.rollback();
                    //e Tentamos fazer a transacao de novo
                    //Exemplo do que poderemos estar a ver
                    //--Performed a Rollback action--
                    //Action   -> sell
                    //SqlState -> 40001
                    //Message  -> ERROR: could not serialize access due to read/write dependencies among transactions
                    //Detail: Reason code: Canceled on identification as a pivot, during write.
                    //Hint: The transaction might succeed if retried.

                    //this session is now in rollback state
                    rollBackState = true;
                    rollback_Count++;
                    continue;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            success_Count++;
            if(rollBackState)
                rollBackState = false;
        }

        try {
            conn.CloseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 16; i++) {
            new Main().start();
        }

        Thread.sleep(2000);

        ColorfulConsole.WriteLine(Green(Underline),"2 threads running");

        String fs = String.format("Rollbacks -> {0}%d\n{1}Succeed -> {2}%d", rollback_Count, success_Count);
        ColorfulConsole.WriteLineFormatted(fs, Red(Bold), White(Regular),Green(Bold));

        float f = (float)rollback_Count / (float)success_Count;
        ColorfulConsole.WriteLine(White(Regular), "Ratio -> " + f + " Rollbacks per Succeed Transaction");

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
