package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Random;

public class Product extends Table {

    public Product(DbConnection connection, String tableName) {
        super(connection, tableName);
    }

    final String Id = "Id";
    final String Desc = "Description";
    final String Stock = "Stock";
    final String Min = "Min";
    final String Max = "Max";

    @Override
    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception {
        if(clearBeforePopulate)
            ClearTable();
        for (int i = 0; i < entries; i++)
        {
            int min = new Random().nextInt(100);
            int max = min + new Random().nextInt(1000);
            int stock = min + new Random().nextInt(max - min);

            try {
                Insert(i, "This is a product", stock, min, max);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int GetProductStock(int productId) throws Exception {
        String formatted = String.format("select %s from %s where %s = ?", Stock, name,  Id);
        ResultSet set = SendQuery(formatted, productId);
        set.next();
        return set.getInt(1);
    }

    public AbstractMap.SimpleEntry<Integer,Integer> GetProductMinMax(int ProductId) throws Exception
    {
        ResultSet set = SendQuery("select ?,? from ? where ? = ?", Min, Max, name, Id, ProductId);
        int x, y;
        set.first();
        x = set.getInt(1);
        y = set.getInt(2);
        return new AbstractMap.SimpleEntry<>(x, y);
    }
}
