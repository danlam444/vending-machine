package com.lammyhill.vendingmachine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AcceptedCoinLoader implements iAcceptedCoinLoader{

    public ArrayList<Coin> loadAcceptedCoins(){
        ArrayList<Coin> acceptedCoins = new ArrayList<Coin>();

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(getClass().getClassLoader().getResource("accepted-coins.json").getFile()));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray acceptedCoinsJSONArray = (JSONArray) jsonObject.get("accepted_coins");
            for(Object o : acceptedCoinsJSONArray){
                JSONObject jsonCoin = (JSONObject) o;
                acceptedCoins.add(new Coin((String) jsonCoin.get("denomination"),  ((Long) jsonCoin.get("value")).intValue()));
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
        return acceptedCoins;
    }
}
