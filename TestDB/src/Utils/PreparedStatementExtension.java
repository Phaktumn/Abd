package Utils;

import java.sql.PreparedStatement;

public class PreparedStatementExtension {

    public static void Set(PreparedStatement preparedStatement, int parameterIndex , Object value) throws Exception {
        if(preparedStatement == null)
            throw new NullPointerException();
        if(parameterIndex < 0)
            throw new IndexOutOfBoundsException();
        if(value == null)
            throw new NullPointerException();
        if(value instanceof Double)
        {
            preparedStatement.setDouble(parameterIndex, (Double) value);
            return;
        }
        if(value instanceof Integer)
        {
            preparedStatement.setInt(parameterIndex, (Integer) value);
            return;
        }
        if(value instanceof String)
        {
            preparedStatement.setString(parameterIndex, (String) value);
            return;
        }
        if(value instanceof Float)
        {
            preparedStatement.setFloat(parameterIndex, (Float) value);
            return;
        }
    }
}
