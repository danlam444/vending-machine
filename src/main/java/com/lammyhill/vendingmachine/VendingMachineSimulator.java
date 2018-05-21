package com.lammyhill.vendingmachine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Core Vending Machine Engine.
 * Simulates behaviour or a vending machine.
 * Limitations: Assumes unlimited change available to the machine!
 */
public class VendingMachineSimulator implements iVendingMachine{

    private int balance;
    private ArrayList<Coin> acceptedCoins;
    private ArrayList<Product> supportedProducts;
    private iInventory inventory;
    private iAcceptedCoinLoader acceptedCoinLoader;

    public VendingMachineSimulator(iAcceptedCoinLoader acceptedCoinLoader, iInventory inventory){
        balance = 0;
        this.acceptedCoinLoader = acceptedCoinLoader;
        acceptedCoins = acceptedCoinLoader.loadAcceptedCoins();
        this.inventory = inventory;
        supportedProducts = inventory.loadSupportedProducts();
    }

    public void insertCoin(Coin coin) throws UnrecognizedCoinException{
        if (acceptedCoins.contains(coin)) {
            balance = balance + coin.getValue();
        }
        else{
            throw new UnrecognizedCoinException();
        }
    }

    public int getBalance(){
        return balance;
    }

    public boolean purchaseProduct(Product product) throws UnrecognizedProductException, InsufficientFundsException {
        if(!supportedProducts.contains(product)){
            throw new UnrecognizedProductException();
        }
        if (balance >= product.getPrice()) {
            inventory.takeProduct(product);
            balance = balance - product.getPrice();
            inventory.takeProduct(product);
            return true;
        }
        throw new InsufficientFundsException();
    }

    public ArrayList<Coin> returnBalance(){
        ArrayList<Coin> changeInCoins = getChangeInCoins(balance, acceptedCoins);
        balance = 0;
        return changeInCoins;
    }

    private ArrayList<Coin> getChangeInCoins(int amount, ArrayList<Coin> acceptedCoins){
        ArrayList<Coin> reverseOrderedCoins = new ArrayList<>(acceptedCoins);

        Collections.sort(reverseOrderedCoins, Collections.reverseOrder()); //reversing order to use biggest coins first
        ArrayList<Coin> coins = new ArrayList<Coin>();

        for(Coin coin : reverseOrderedCoins){
            int numberCoins = amount / coin.getValue();
            for(int i=0; i<numberCoins; i++){
                coins.add(coin);
            }

            amount = amount % coin.getValue();
            if(amount == 0){break;}
        }
        return coins;
    }

    public void reset(){
        balance = 0;
        acceptedCoins = acceptedCoinLoader.loadAcceptedCoins();
        supportedProducts = inventory.loadSupportedProducts();
    }

    public ArrayList<Coin> getAcceptedCoins() {
        return acceptedCoins;
    }

    public ArrayList<Product> getSupportedProducts() {
        return supportedProducts;
    }
}
