package com.company.Exception;

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
