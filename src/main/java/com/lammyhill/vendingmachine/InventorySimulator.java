package com.lammyhill.vendingmachine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class InventorySimulator  implements iInventory{

    private ArrayList<Product> supportedProducts;

    public InventorySimulator(){
        setSupportedProducts(loadSupportedProducts());

    }

    public ArrayList<Product> loadSupportedProducts(){
        ArrayList<Product> supportedProducts = new ArrayList<Product>();

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(getClass().getClassLoader().getResource("products.json").getFile()));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray acceptedCoinsJSONArrayJSONArray = (JSONArray) jsonObject.get("products");
            for(Object o : acceptedCoinsJSONArrayJSONArray){
                JSONObject jsonCoin = (JSONObject) o;
                supportedProducts.add(new Product((String) jsonCoin.get("name"),  ((Long) jsonCoin.get("price")).intValue()));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Problem finding file");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Problem reading file");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Problem parsing file");
            e.printStackTrace();
        }
        return supportedProducts;
    }

    /*
     * This Inventory Simulator gives/vends a product if it's a supported product.
     * Practical implementations may also take a count of number of times product is purchased for sales & marketing
     * purposes.
     */
    public boolean takeProduct(Product product){
        return (isInStock(product));
    }

    public boolean addProduct(Product product){
        return false;
    }

    public ArrayList<Product> getSupportedProducts() {
        return supportedProducts;
    }

    /*
    * As this is implementation is an Inventory Simulator it simulates the products as always being in stock if
    * its supported.
    * A different implementation of the interface could have this method integrate with a sensor in a physical
    * vending machine that detects whether a product is in stock (on a tray).
    */
    public void setSupportedProducts(ArrayList<Product> supportedProducts) {
        this.supportedProducts = supportedProducts;
    }

    public boolean isInStock(Product product){
        return getSupportedProducts().contains(product);
    }
}
