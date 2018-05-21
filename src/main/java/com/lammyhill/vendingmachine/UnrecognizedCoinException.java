package com.lammyhill.vendingmachine;

public class UnrecognizedCoinException extends Exception {

    public UnrecognizedCoinException(){}

    public UnrecognizedCoinException(String message){
        super(message);
    }
}
