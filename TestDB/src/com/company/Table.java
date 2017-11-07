package com.company;

import Utils.PreparedStatementExtension;
import com.company.DataBase.Parameters.TableParameters;
import javafx.scene.control.Tab;
import org.postgresql.util.PSQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class Table {

    protected DbConnection connection;

    public TableParameters parameters = new TableParameters();

    protected int Size;

    public int getSize() throws Exception {
        PreparedStatement ps = connection.GetConnection().prepareStatement("select count(*) from " + name);
        SendQuery(ps);
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
        PreparedStatement ps = connection.GetConnection()
                .prepareStatement(String.format("delete from %s;", name));
        SendQuery(ps);
        connection.Commit();
        Size = 0;
    }

    public void Populate(int entries, Boolean clearBeforePopulate) throws Exception {
        connection.Commit();
    }

    public PreparedStatement PrepareInsertStatement() throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        String query = String.format("insert into %s(", name);
        queryBuilder.append(query);

        Object[] arr = parameters.asArray();
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == parameters.getPkName()){
                continue;
            }
            queryBuilder.append(String.format("%s,", (String) arr[i]));
        }

        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));

        queryBuilder.append(") values(");

        for (int i = 0; i < parameters.Count() - 1; i++) {
            queryBuilder.append("?,");
        }

        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(","));
        queryBuilder.append(");");
        return connection.GetConnection().prepareStatement(queryBuilder.toString());
    }

    /**
     * @param args Arguments to substitute {?}
     * @return ResultSet if the Query returns a valid value else returns NULL
     * @throws Exception
     */
    public ResultSet SendQuery(PreparedStatement preparedStatement, Object... args) throws Exception {
        counter = 1;
        PreparedStatement ps = preparedStatement;
        for (Object o : args){
            PreparedStatementExtension.Set(ps, counter, o);
            counter++;
        }
        ResultSet resultSet;

        ps.execute();
        resultSet = ps.getResultSet();
        return resultSet;
    }

    /** This method requires you to call {@code executeBatch()} */
    public PreparedStatement SendBatch(PreparedStatement preparedStatement, Object... args) throws Exception{
        counter = 1;
        PreparedStatement ps = preparedStatement;
        for (Object o : args) {
            PreparedStatementExtension.Set(ps, counter, o);
            counter++;
        }
        ps.addBatch();
        return ps;
    }

    public PreparedStatement PrepareUpdateStatement(String IDColumnName, String[] columnNames) throws SQLException {
        String queryPart = String.format("update %s set ", name);
        for (int i = 0; i < columnNames.length; i++) {
            if(i == columnNames.length - 1){
                queryPart += (columnNames[i] + "= ?");
            }
            else queryPart += (columnNames[i] + "= ?,");
        }
        String query = String.format("%s where %s = ?", queryPart, IDColumnName);
        return connection.GetConnection().prepareStatement(query);
    }
}
