package com.company.DataBase;

import com.company.DataBase.Parameters.TableParameters;
import com.company.DbConnection;
import com.company.Table;
import com.company.Utilities.ColorfulConsole;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import static com.company.Utilities.ConsoleColors.AnsiColor.Green;
import static com.company.Utilities.ConsoleColors.AnsiColor.Modifier.Bold;
import static com.company.Utilities.ConsoleColors.AnsiColor.Red;
import static com.company.Utilities.ConsoleColors.AnsiColor.Yellow;

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
                ResultSet set = SendQuery(true, statement, personName,location);
                set.next();
                lastInserted_ID = set.getInt(1);
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
            String resString = String.format("We are low on stock" +
                    "\n\tRequired->%d" +
                    "\n\tAvailable->%d", invoiceLines, res);
            ColorfulConsole.WriteLine(Red(Bold),resString);
            return;
        }

        PreparedStatement statement = invoices.PrepareInsertStatement();
        ResultSet rs = SendQuery(true, statement, clientId);
        rs.next();
        int autoGenKeys = rs.getInt(1);
        //statement.executeBatch();

        statement = lines.PrepareInsertStatement();
        for (int i = 0; i < invoiceLines; i++){
            SendQuery(false, statement, autoGenKeys, productId);
        }

        p.SendQuery(false, p.PrepareUpdateStatement(p.parameters.GetParameter("id"),
                new String[] { p.parameters.GetParameter("stock")}), res - invoiceLines, productId);

        ColorfulConsole.WriteLine(Green(Bold),"--Client Performed a Sell Action--");
    }
}
