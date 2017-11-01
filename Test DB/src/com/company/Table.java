package com.company;

import Utils.PreparedStatementExtension;
import javafx.scene.control.Tab;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Table {

    protected DbConnection connection;

    protected int Size;
    public int getSize() throws Exception {
        SendQuery("select count(*) from " + name);
        connection.Commit();
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
    public void ClearTable() throws Exception {
        SendQuery(String.format("delete from %s;", name));
        connection.Commit();
        Size = 0;
    }

    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception { }

    public void Insert(Object... args) throws Exception {
        StringBuilder queryBuilder = new StringBuilder();
        String query = String.format("insert into %s values(", name);
        queryBuilder.append(query);

        Arrays.stream(args).map(o -> "?,").forEach(queryBuilder::append);

        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));
        queryBuilder.append(");");

        SendQuery(queryBuilder.toString(), args);
    }

    /**
     * @param query SQL query to send using {?} to format
     * @param args Arguments to substitute {?}
     * @return ResultSet if the Query returns a valid value else returns NULL
     * @throws Exception
     */
    public ResultSet SendQuery(String query, Object... args) throws Exception {

        //match args count with the number of ? in the query
        int counter = 0;
        for (char c : query.trim().toCharArray()){
            if(c == '?'){
                counter++;
            }
        }
        if(counter != args.length)
            throw new Exception("The number of arguments does not mach the number of {?} in the query");

        if(!query.endsWith(";"))
            query += ";";
        counter = 1;
        PreparedStatement ps = connection.GetConnection().prepareStatement(query);
        for (Object o : args){
            PreparedStatementExtension.Set(ps, counter, o);
            counter++;
        }
        ResultSet resultSet = null;
        try {
            resultSet = ps.executeQuery();
        }
        catch (Exception e) {
            try {
                ps.executeUpdate();
                ps.close();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        connection.Commit();
        return resultSet;
    }

    public void UpdateField(String IDColumnName, String[] columnNames, Object... args) throws Exception {
        //update product set stock = ? where id = ?;
        String queryPart = String.format("update %s set ", name);
        for (int i = 0; i < columnNames.length; i++) {
            if(i == columnNames.length - 1){
                queryPart += (columnNames[i] + "= ?");
            }
            else queryPart += (columnNames[i] + "= ?,");
        }
        String query = String.format("%s where %s = ?", queryPart, IDColumnName);
        SendQuery(query, args);
    }
}
