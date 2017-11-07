package com.company.DataBase.Parameters;

import com.company.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableParameters implements Iterable {

    private List<String> parameters;

    @Override
    public Iterator iterator() {
        return parameters.iterator();
    }

    public TableParameters(){
        parameters = new ArrayList<>();
    }

    public void Insert(String parameter)
    {
        parameter = parameter.toLowerCase();
        parameters.add(parameter);
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
}
