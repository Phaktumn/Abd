package com.company.DataBase.Parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableParameters implements Iterable {

    private List<String> parameters;

    private int pkIndex;
    private String pkName;

    @Override
    public Iterator iterator() {
        return parameters.iterator();
    }

    public TableParameters(){
        parameters = new ArrayList<>();
    }

    public Object[] asArray(){
        return parameters.toArray();
    }

    public void Insert(String parameter, boolean isPrimaryKey)
    {
        parameter = parameter.toLowerCase();
        if(isPrimaryKey){
            pkIndex = Count();
            pkName = parameter;
        }
        parameters.add(parameter);
    }

    public void Insert(String param) {
        Insert(param, false);
    }

    public String GetParameter(String string){
        string = string.toLowerCase();
        int index = parameters.indexOf(string);
        if(index == -1)
            return "";
        else return parameters.get(index);
    }

    public int Count(){
        return parameters.toArray().length;
    }

    public int getPkIndex() {
        return pkIndex;
    }

    public String getPkName() {
        return pkName;
    }
}
