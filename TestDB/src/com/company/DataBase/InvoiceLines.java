package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

import java.sql.PreparedStatement;

public class InvoiceLines extends Table {

    public InvoiceLines(DbConnection connection, String tableName) {
        super(connection, tableName);
        parameters.Insert("id", true);
        parameters.Insert("invoiceid");
        parameters.Insert("productid");
    }

    public void ListTopNMostSold(Product p, int n) throws Exception
    {
        String format = String.format("select product.%s,count(%s) as i from invoiceline,product where" +
                " product.%s = productid group by product.id order by i desc limit %d;",
                p.parameters.GetParameter("id"), p.parameters.GetParameter("id"), parameters.asArray()[2], n);
        PreparedStatement ps = connection.GetConnection().prepareStatement(format);
        //ResultSet set = SendQuery(ps);
        //ColorfulConsole.WriteLine(Green(Bold),"--Listing Top 10--");
        //while (set.next()){
        //    System.out.println(set.getInt(1) + "|" + set.getString(2));
        //}
    }
}
