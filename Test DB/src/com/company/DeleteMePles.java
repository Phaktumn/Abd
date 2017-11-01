package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class DeleteMePles extends Thread {

    // Gerar o numero da fatura
    private static int n = 0;

    private static synchronized int next() {
        return n++;
    }

    private static long ultima = -1;

    private static long iaa = 0, tra = 0, c = 0;
    private static boolean start = false;

    private static synchronized void regista(long antes, long depois) {


        long tr = depois-antes;

        long anterior = ultima;
        ultima = depois;

        if (anterior < 0 || !start)
            return;

        long ia = depois - anterior;

        iaa += ia;
        tra += tr;
        c++;
    }

    public static synchronized void partida() {
        start = true;
    }

    public static synchronized void imprime() {
        double trm = (tra/1e9d)/c;
        double debito = 1/((iaa/1e9d)/c);

        System.out.println("debito = "+debito+" tps, tr = "+trm+" s");

    }

    public static Connection conn;

    public void run() {


    }

    static void Populate(Connection connection) throws SQLException {
        /*
        * Client: Id, Name, Address.
        Product: Id, Description, Stock, Min, Max.
        Invoice: Id, ProductId, ClientId.
        InvoiceLine: Id, InvoiceId, ProductId.
        Order: Id, ProductId, Supplier, Items.
        This application should provide the following operations
        Delivery: For
        * */

        PreparedStatement ps;
        for (int i = 0; i < 10; i++)
        {
            int r = new Random().nextInt(10000);
            int name = new Random().nextInt(100000);

            ps = connection.prepareStatement("insert into Client values(?, ?, ?);");
            ps.setInt(1, i);
            ps.setString(2, "Person[" + name + "]");
            ps.setString(3, "Portugal");
            ps.executeUpdate();
        }

        for (int i = 0; i < 10 ; i++)
        {
            int r = new Random().nextInt(10000);
            int min = new Random().nextInt(100);
            int max = min + new Random().nextInt(1000);
            int stock = min + new Random().nextInt(max - min);

            ps = connection.prepareStatement("insert into Product values(?,?,?,?,?);");
            ps.setInt(1, i);
            ps.setString(2, "This is a product");
            ps.setInt(3, stock);
            ps.setInt(4, min);
            ps.setInt(5, max);
            ps.executeUpdate();
        }
    }

    /*
        Sell: Add invoice record, with multiple invoice lines and decrease corresponding stock
        values.
    */
    static  public void Sell(int clientId, int productId) throws Exception {
        int id = new Random().nextInt(10000);
        int invoiceLines = 5 + new Random().nextInt(15);

        PreparedStatement ps ;

        ps = conn.prepareStatement("select stock from product where id = ?;");
        ps.setInt(1, productId);
        ps.execute();
        System.out.println("select stock from product where id = ?;");

        ps.getResultSet().next();
        int res = ps.getResultSet().getInt(1);
        if(res - invoiceLines < 0){
            throw new Exception();
        }

        ps = conn.prepareStatement("insert into invoice values(?,?);");
        ps.setInt(1, id);
        ps.setInt(2, clientId);
        ps.executeUpdate();
        System.out.println("insert into invoice values(?,?,?);");

        for (int i = 0; i < invoiceLines; i++) {
            //ID INVOICEID PRODUCTID
            ps = conn.prepareStatement("insert into invoiceline values(?,?,?);");
            ps.setInt(1, i);
            ps.setInt(2, id);
            ps.setInt(3, productId);
            ps.executeUpdate();
            System.out.println("insert into invoiceline(?,?,?);");

        }

        ps = conn.prepareStatement("update product set stock = ? where id = ?;");
        ps.setInt(1, res - invoiceLines);
        ps.setInt(2, productId);
        ps.executeUpdate();
        System.out.println("update product set stock = ? where id = ?;");
    }

    /*Account: List items sold to that client.*/
    static public void List(int clientId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select id from invoices where if = ?;");
        ps.setInt(1, clientId);
        ps.executeUpdate();
        while(ps.getResultSet().next()){
            System.out.println(ps.getResultSet().getArray(0));
        }
    }

    /*Top10: List currently 10 most sold products*/
    public void Top10(){

    }

    /*Order: Place an order for a product where Stock < Min. This should create or update
        one order record for Max - Stock items.*/
    public void Order(){

    }

    public static void main(String[] args) throws Exception {
        Random r = new Random();

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/invoices");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        Populate(conn);
        conn.commit();
        try {
            Sell(5, 2);
            Sell(5, 6);
            Sell(5, 8);
            Sell(5, 2);
            conn.commit();
            System.out.println("Commit");
        }
        catch (Exception e){
            conn.rollback();
            e.printStackTrace();
            System.out.println("Rollback");
        }
    }
}