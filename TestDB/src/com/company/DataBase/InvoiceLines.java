package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InvoiceLines extends Table {

    public InvoiceLines(DbConnection connection, String tableName) {
        super(connection, tableName);
    }

    public void ListTopNMostSold(Product p, int n) throws Exception
    {
        String format = String.format("select product.%s,count(invoiceline.productid) as i from invoiceline,product where" +
                " product.%s = productid group by product.id order by i desc limit %d;",
                p.parameters.GetParameter("id"), p.parameters.GetParameter("id"), n);
        PreparedStatement ps = connection.GetConnection().prepareStatement(format);
        ResultSet set = SendQuery(ps);
        System.out.println("--Listing Top 10--");
        //while (set.next()){
        //    System.out.println(set.getInt(1) + "|" + set.getString(2));
        //}
    }
}
