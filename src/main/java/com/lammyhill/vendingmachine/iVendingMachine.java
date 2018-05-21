package com.lammyhill.vendingmachine;

import java.util.ArrayList;

interface iVendingMachine {

    void insertCoin(Coin coin) throws UnrecognizedCoinException;

    ArrayList<Coin> returnBalance();

    void reset();
}
