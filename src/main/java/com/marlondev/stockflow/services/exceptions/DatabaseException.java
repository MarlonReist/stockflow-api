package com.marlondev.stockflow.services.exceptions;

public class DatabaseException extends RuntimeException{

    public DatabaseException() {
        super("Esse cpf já existe");
    }
}
