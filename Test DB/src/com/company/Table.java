package com.company;

import Utils.PreparedStatementExtension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Table {

    protected DbConnection connection;
    protected int Size;
    public int getSize() throws Exception {
        Object[] args = { name };
        SendQuery("select count(*) from ?", args);
        return Size;
    }

    protected String name;
    public String getName() {
        return name;
    }

    protected int counter = 0;

    public Table(DbConnection connection, String tableName){
        this.connection = connection;
        this.name = tableName;
    }

    /*
    * This method clears the whole table
    * all information is lost
    * */
    public void ClearTable(String tableName) throws SQLException {
        PreparedStatement ps = connection.GetConnection().prepareStatement("delete from " + tableName + ";");
        ps.executeUpdate();
        Size = 0;
    }

    public abstract void Populate(int entries, Boolean clearBeforePopulate) throws SQLException;

    public ResultSet SendQuery(String query, Object... args) throws Exception {

        if(!query.endsWith(";"))
            query += ";";
        int counter = 1;
        PreparedStatement ps = connection.GetConnection().prepareStatement(query);
        for (Object o : args){
            PreparedStatementExtension.Set(ps, counter, o);
            counter++;
        }
        ResultSet resultSet =  ps.executeQuery();
        return resultSet;
    }
}
