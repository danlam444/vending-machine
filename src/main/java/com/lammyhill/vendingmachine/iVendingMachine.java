package com.lammyhill.vendingmachine;

import java.util.ArrayList;

interface iVendingMachine {

    void insertCoin(Coin coin) throws UnrecognizedCoinException;

    ArrayList<Coin> returnBalance();

    void reset();

    int getBalance();

    ArrayList<Coin> getAcceptedCoins();

    ArrayList<Product> getSupportedProducts();

    boolean purchaseProduct(Product product) throws UnrecognizedProductException, InsufficientFundsException;
}
