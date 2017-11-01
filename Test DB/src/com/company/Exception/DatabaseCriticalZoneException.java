package com.company.Exception;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DatabaseCriticalZoneException extends Exception {

    String reason;

    public DatabaseCriticalZoneException(String reason){
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason;
    }

    @Override
    public void printStackTrace() {
        System.out.println(reason);
        super.printStackTrace();
    }
}
