package com.company.DataBase;

import com.company.DataBase.Parameters.TableParameters;
import com.company.DbConnection;
import com.company.Table;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Client extends Table {

    public Client(DbConnection connection, String tableName) {
        super(connection, tableName);

        parameters = new TableParameters();
        parameters.Insert("id", true);
        parameters.Insert("name");
        parameters.Insert("addrs");
    }

    @Override
    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception
    {
        if(clearBeforePopulate)
            ClearTable();

        PreparedStatement statement = PrepareInsertStatement();

        for (int i = 0; i < entries; i++) {
            int name = new Random().nextInt(100000);

            String personName = "Person[" + name + "]";
            String location = "Portugal";
            try {
                //SendBatch(statement, i, personName, location);
                SendQuery(statement, personName,location);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //statement.executeBatch();
        super.Populate(entries, clearBeforePopulate);
        /* Populate(entries - 1, clearBeforePopulate); */
    }

    public void Sell(int clientId,  int productId, Product p, InvoiceLines lines, Invoices invoices) throws Exception {
        int id = new Random().nextInt(10000);
        int invoiceLines = 5 + new Random().nextInt(15);

        //ResultSet set = SendQuery("select stock from product where id = ?;", productId);
        int res = p.GetProductStock(productId);
        if(res - invoiceLines < 0){
            throw new PSQLException("Low Stock", PSQLState.NUMERIC_CONSTANT_OUT_OF_RANGE);
        }

        PreparedStatement statement = invoices.PrepareInsertStatement();
        SendQuery(statement, clientId);
        //statement.executeBatch();

        statement = lines.PrepareInsertStatement();
        for (int i = 0; i < invoiceLines; i++){
            SendQuery(statement, new Random().nextInt(10000) + 100, id, productId);
        }

        p.SendQuery(p.PrepareUpdateStatement(p.parameters.GetParameter("id"),
                new String[] { p.parameters.GetParameter("stock")}), res - invoiceLines, productId);

        System.out.println("--Client Performed a Sell Action--");
    }
}
