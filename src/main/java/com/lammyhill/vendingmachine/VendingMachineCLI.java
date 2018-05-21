package com.lammyhill.vendingmachine;

import java.util.ArrayList;
import java.util.Scanner;

/*TODO
Refactor methods in here to take input and output streams as parameters to make them more testable
e.g (InputStream in,PrintStream out)
 */

/**
 * 
 */
public class VendingMachineCLI {
    VendingMachineSimulator vendingMachineSimulator;
    Scanner command;

    public VendingMachineCLI(){
        vendingMachineSimulator = new VendingMachineSimulator(new AcceptedCoinLoader(), new InventorySimulator());
        command = new Scanner(System.in);
    }

    public void runVendingMachine(){

        boolean run = true;
        while(run){

            System.out.println("\n\n[Vending Machine Simulator] \n" +
                                "Current balance is: " + vendingMachineSimulator.getBalance() + "\n" +
                                "Please pick one of the following options: \n" +
                                "1. Insert a coin \n" +
                                "2. Purchase a product \n" +
                                "3. Return balance \n" +
                                "4. Reset vending machine \n" +
                                "5. Exit Vending Machine Simulator \n"
            );

            switch(command.nextLine()){

                case "1":
                    insertCoin();
                    break;

                case "2":
                    purchaseProduct();
                    break;

                case "3":
                    returnBalance();
                    break;

                case "4":
                    reset();
                    break;

                case "5":
                    run = false;
                    break;

                default:
                    System.out.println("Command not recognized!");
                    break;
            }
        }
    }

    public void insertCoin(){
        System.out.println("Select a coin to insert:");
        ArrayList<Coin> acceptedCoins =  vendingMachineSimulator.getAcceptedCoins();
        for(int i=0; i<acceptedCoins.size(); i++){
            System.out.println(i+1 + ". " + acceptedCoins.get(i).getValue() + " " + acceptedCoins.get(i).getDenomination());
        }

        int optionNumber = Integer.parseInt(command.nextLine());
        if(optionNumber < 1 || optionNumber > acceptedCoins.size()){
            System.out.println("Command not recognized!");
        }
        else{
            try {
                vendingMachineSimulator.insertCoin(acceptedCoins.get(optionNumber - 1));
            }
            catch(UnrecognizedCoinException e){
                System.out.println("Vending Machine does not accept this coin. " + e.getMessage());
            }
            System.out.println("Coin accepted");
            System.out.println("Your updated balance is: " + vendingMachineSimulator.getBalance());
        }
    }

    public void purchaseProduct(){
        System.out.println("Your balance is: " + vendingMachineSimulator.getBalance());
        System.out.println("Select a product to purchase:");
        ArrayList<Product> products =  vendingMachineSimulator.getSupportedProducts();
        for(int i=0; i<products.size(); i++){
            System.out.println(i+1 + ". " + products.get(i).getName() + " " + products.get(i).getPrice());
        }

        int optionNumber = Integer.parseInt(command.nextLine());
        if(optionNumber < 1 || optionNumber > products.size()){
            System.out.println("Command not recognized!");
        }
        else{
            try {
                Product product = products.get(optionNumber - 1);

                boolean confirmPurchase = confirmPurchase(product);

                if(confirmPurchase){
                    vendingMachineSimulator.purchaseProduct(product);
                    ArrayList<Coin> changeInCoins = vendingMachineSimulator.returnBalance();

                    System.out.println("Purchased: " + product.getName());
                    System.out.println("Your change is given back in the following coin(s):");
                    for(Coin coin : changeInCoins){
                        System.out.println(coin.getValue() + " " + coin.getDenomination());
                    }
                }
                else{
                   System.out.print("Purchase cancelled.");
                }


            }
            catch(UnrecognizedProductException e){
                System.out.println("Vending Machine does not sell this product.");
            }
            catch(InsufficientFundsException e){
                System.out.println("You have insufficient funds to purchase this product.");
            }
        }
    }

    public boolean confirmPurchase(Product product) {
        System.out.println("Are you sure you want to purchase " + product.getName() + "? (y/n)");

        while (true) {
            switch (command.nextLine()) {

                case "y":
                    return true;

                case "n":
                    return false;

                default:
                    System.out.println("Command not recognized! Please enter y for yes or n for no");
                    break;
            }
        }
    }

    public void reset(){
        System.out.println("Resetting Vending Machine...");
        vendingMachineSimulator.reset();
    }

    public void returnBalance(){
        if(vendingMachineSimulator.getBalance() > 0) {
            System.out.println("Your balance is given back in the following coin(s):");
            ArrayList<Coin> changeInCoins = vendingMachineSimulator.returnBalance();
            for (Coin coin : changeInCoins) {
                System.out.println(coin.getValue() + " " + coin.getDenomination());
            }
        }
        else{
            System.out.println("There is no balance to return");
        }
    }

}
