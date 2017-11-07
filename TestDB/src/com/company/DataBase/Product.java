package com.company.DataBase;

import com.company.DataBase.Parameters.TableParameters;
import com.company.DbConnection;
import com.company.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.Random;

public class Product extends Table {

    public Product(DbConnection connection, String tableName) {
        super(connection, tableName);

        parameters.Insert("id", true);
        parameters.Insert("descr");
        parameters.Insert("stock");
        parameters.Insert("min");
        parameters.Insert("max");
    }

    @Override
    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception {
        if(clearBeforePopulate)
            ClearTable();

        PreparedStatement statement = PrepareInsertStatement();
        statement.closeOnCompletion();

        for (int i = 0; i < entries; i++)
        {
            int min = new Random().nextInt(100);
            int max = min + new Random().nextInt(1000);
            int stock = min + new Random().nextInt(max - min);

            try {
                SendQuery(statement, "This is a product", stock, min, max);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //statement.executeBatch();
        super.Populate(entries, clearBeforePopulate);
    }

    public int GetProductStock(int productId) throws Exception {
        String formatted = String.format("select %s from %s where %s = ?", parameters.GetParameter("stock"),
                name,  parameters.GetParameter("id"));
        PreparedStatement statement = connection.GetConnection().prepareStatement(formatted);
        ResultSet set = SendQuery(statement, productId);
        set.next();
        int i = set.getInt(1);
        return i;
    }

    public AbstractMap.SimpleEntry<Integer,Integer> GetProductMinMax(int ProductId) throws Exception
    {
        PreparedStatement ps = connection.GetConnection()
                .prepareStatement(String.format("select %s,%s from %s where %s = ?",
                        parameters.GetParameter("min"), parameters.GetParameter("max"),
                        name, parameters.GetParameter("id")));
        ResultSet set = SendQuery(ps, ProductId);
        int x, y;
        set.first();
        x = set.getInt(1);
        y = set.getInt(2);
        return new AbstractMap.SimpleEntry<>(x, y);
    }
}
