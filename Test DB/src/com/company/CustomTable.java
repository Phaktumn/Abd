package com.company;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class CustomTable extends Table
{
    public CustomTable(DbConnection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    public void Populate(int entries, Boolean clearBeforePopulate) throws SQLException {
        if(clearBeforePopulate)
            ClearTable(super.name);
        super.counter = 0;

        PreparedStatement ps = connection.GetConnection().prepareStatement("insert into " + super.name + " values (?, ?);");
        while (super.counter < entries)
        {

            ps.setInt(1, counter);
            ps.setInt(2, new Random().nextInt(entries));
            ps.executeUpdate();
            counter++;
        }
        counter = 0;
    }
}
