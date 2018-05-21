package com.lammyhill.vendingmachine;

public class Coin implements Comparable<Coin>{
    private String denomination;
    private int value;

    public Coin(String denomination, int value){
        this.denomination = denomination;
        setValue(value);
    }

    public int getValue(){
        return value;
    }

    public String getDenomination(){
        return denomination;
    }

    private void setValue(int value) {
        if (value < 0){
            throw new IllegalArgumentException("value cannot be negative " + value);
        }
        this.value = value;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        Coin coinB = (Coin) obj;
        if (coinB.getValue() == getValue() && coinB.getDenomination().equals(getDenomination())){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Coin coinB){
        if (value < coinB.getValue()){
            return -1;
        }
        else if (value > coinB.getValue()){
            return 1;
        }
        else{
            return 0;
        }
    }

}
