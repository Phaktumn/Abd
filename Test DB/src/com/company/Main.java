package com.company;

import com.sun.javafx.scene.layout.region.Margins;
import javafx.util.converter.LocalDateStringConverter;
import jdk.nashorn.internal.runtime.NumberToString;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class Main extends Thread {

    private static int count = 0;
    private static int n = ((int) Math.pow(2, 4));
    private static Random rand = null;

    private static List<Integer> compras = new ArrayList<Integer>();

    private static long ultima = -1;
    private static long iaa = 0, tra = 0, c= 0;
    private static boolean start = false;

    private static int s = 0;
    private static synchronized int next() {return s++;}

    private static synchronized void Regista(long antes, long depois){
        long tr = depois-antes;
        long anterior = ultima;
        ultima = depois;
        if(anterior < 0 || start == false)
            return;
        long ia = depois - anterior;
        iaa += ia;
        tra += tr;
        c++;
    }

    private static synchronized void partida(){
        start = true;
    }

    private static synchronized void imprime(){
        double trm = (tra/1e9d)/c;
        double debito = 1/((iaa/1e9d)/c);
        out.println("Debito = " + debito + "tps, tr = " + trm + "s");
    }

    public void run(){
        DbConnection conn = new DbConnection();
        try {
            conn.Connect("Aula3DB", 5433);
            CustomTable X = new CustomTable(conn, "X");
            CustomTable Y = new CustomTable(conn, "Y");
            CustomTable Z = new CustomTable(conn, "Z");
            X.Populate(100, true);
            Y.Populate(100, true);
            Z.Populate(100, true);
            try {
                ResultSet set =  X.SendQuery("select a from X where b = ?", new Object[]{ 8 });
                while (set.next()){
                    out.println("\t" + set.getString("a"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

        //while(true)
        //{
        //    long antes = System.nanoTime();

        //    if(start)
        //    {
        //        while(count <= n){
        //            try {
        //                Populate(conn.GetConnection());
        //            } catch (SQLException e) {
        //                e.printStackTrace();
        //            }
        //            count++;
        //        }
        //    }

        //    long depois = System.nanoTime();
        //    Regista(antes,depois);
        //}
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException
    {
        int threads = 1;

        for (int i = 0; i < threads; i++) {
            new Main().start();
        }
        Thread.sleep(1000);
        System.exit(0);
       /* c = 0;
        int compraN = 0;
        while(c <= n){
            int res = rand.nextInt(3);
            PreparedStatement ps;
            switch (res){
                case 0:
                    out.println("\n--Listagem de Clientes--");
                    String query = "select nome from cliente where id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setInt(1, rand.nextInt(n));
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()){
                        out.println("\t" + rs.getString("nome"));
                    }
                    break;
                case 1:
                    out.println("\n!--Compra Efetuada--!");
                    Buy(compraN++, rand.nextInt(n), rand.nextInt(n));
                    break;
                case 2:
                    //Mostrar os n mais comprados
                    int n = 10;
                    out.println("\n--Mais Comprados--");
                    String maisCompradosQuery = String.format(
                            "select idProduto " +
                                    "from invoice " +
                                    "group by idProduto " +
                                    "order by count(idProduto) desc " +
                                    "limit %d;", n);
                    PreparedStatement preparedStatement1 = conn.prepareStatement(maisCompradosQuery);
                    ResultSet rs1 = preparedStatement1.executeQuery();
                    while (rs1.next()){
                        out.println("\t" + rs1.getString("idProduto"));
                    }

                    break;
            }
            c++;
        }*/
    }

    public static void Populate(Connection conn, String tableName) throws SQLException
    {
        //PreparedStatement ps = conn.prepareStatement("insert into cliente values (?, ?);");
        //ps.setInt(1, count);
        //ps.setString(2, "[" + (count | rand.nextInt(10000)) + "]");

        //PreparedStatement ps1 = conn.prepareStatement("insert into produto values (?, ?, ?);");
        //ps1.setInt(1, count);
        //ps1.setString(2, "Alface");
        //ps1.setInt(3, n);

        //ps.executeUpdate();
        //ps1.executeUpdate();
    }


    public static void Buy(int idCompra, int idBuyer, int idProd, Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("insert into invoice values (?, ?, ?);");
        ps.setInt(1, idCompra);
        ps.setInt(2, idBuyer);
        ps.setInt(3, idProd);
        ps.executeUpdate();
    }
}
