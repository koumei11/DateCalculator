package com.example.DateManager;

public class NotExecuteAnyMoreException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotExecuteAnyMoreException(String message){
        super(message);
    }
}
