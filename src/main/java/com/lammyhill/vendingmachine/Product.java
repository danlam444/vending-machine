package com.lammyhill.vendingmachine;

public class Product {

    private String name;
    private int price;

    public Product(String name, int price){
        setName(name);
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        Product productB = (Product) obj;
        if (productB.getPrice() == getPrice() && productB.getName().equals(getName())){
            return true;
        }
        return false;
    }
}
