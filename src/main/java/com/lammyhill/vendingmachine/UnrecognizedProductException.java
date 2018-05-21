package com.lammyhill.vendingmachine;

public class UnrecognizedProductException extends Exception {
    public UnrecognizedProductException(){}

    public UnrecognizedProductException(String message){
        super(message);
    }
}
