package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

import java.sql.PreparedStatement;
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

        PreparedStatement statement = PrepareInsertStatement(3);

        for (int i = 0; i < entries; i++) {
            int name = new Random().nextInt(100000);

            String personName = "Person[" + name + "]";
            String location = "Portugal";
            try {
                SendBatch(statement, i, personName, location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        statement.executeBatch();
        super.Populate(entries, clearBeforePopulate);
        /* Populate(entries - 1, clearBeforePopulate); */
    }

    public void Sell(int clientId,  int productId, Product p) throws Exception {
        int id = new Random().nextInt(10000);
        int invoiceLines = 5 + new Random().nextInt(15);

        //ResultSet set = SendQuery("select stock from product where id = ?;", productId);
        int res = p.GetProductStock(productId);
        if(res - invoiceLines < 0){
            throw new Exception("Stock is lower than the required amount");
        }

        PreparedStatement statement = connection.GetConnection()
                .prepareStatement("insert into invoice values(?,?);");
        SendBatch(statement, id, clientId);
        statement.executeBatch();

        statement = connection.GetConnection().prepareStatement("insert into invoiceline values(?,?,?);");
        for (int i = 0; i < invoiceLines; i++){
            SendBatch(statement, i,id,productId);
        }
        statement.executeBatch();

        p.SendQuery(p.PrepareUpdateStatement(p.Id,new String[] { p.Stock}), res - invoiceLines, productId);
    }
}
