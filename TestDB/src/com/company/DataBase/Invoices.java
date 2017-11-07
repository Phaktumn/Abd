package com.company.DataBase;

import com.company.DbConnection;
import com.company.Table;

public class Invoices extends Table
{
    public Invoices(DbConnection connection, String tableName) {
        super(connection, tableName);
        parameters.Insert("id", true);
        parameters.Insert("clientid");
    }
}
