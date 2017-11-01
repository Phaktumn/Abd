package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

import java.sql.SQLException;
import java.util.Random;

public class Client extends Table {

    public Client(DbConnection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception {
        if(clearBeforePopulate)
            ClearTable();
        for (int i = 0; i < entries; i++) {
            int r = new Random().nextInt(10000);
            int name = new Random().nextInt(100000);

            String personName = "Person[" + name + "]";
            String location = "Portugal";
            try {
                Insert(r, personName, location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* Populate(entries - 1, clearBeforePopulate); */
    }

    public void Sell(int clientId,  int productId, Product p) throws Exception {
        int id = new Random().nextInt(10000);
        int invoiceLines = 5 + new Random().nextInt(15);

        //ResultSet set = SendQuery("select stock from product where id = ?;", productId);
        int res = p.GetProductStock(productId);
        if(p.GetProductStock(productId) - invoiceLines < 0){
            throw new Exception("Stock is lower than the required amount");
        }

        SendQuery("insert into invoice values(?,?);", id, clientId);
        for (int i = 0; i < invoiceLines; i++){
            SendQuery("insert into invoiceline values(?,?,?);", i,id,productId);
        }
        p.UpdateField(p.Id, new String[] { p.Stock}, res - invoiceLines, productId);
    }
}
