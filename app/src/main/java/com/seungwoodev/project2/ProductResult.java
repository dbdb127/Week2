package com.seungwoodev.project2;

import java.util.ArrayList;

public class ProductResult {
    private Integer code;
    private ArrayList<String> name;
    private ArrayList<Integer> price;
    private ArrayList<Integer> qty;

    public Integer getCode(){return code;}
    public ArrayList<String> getName() {
        return name;
    }
    public ArrayList<Integer> getPrice(){
        return price;
    }
    public ArrayList<Integer> getQty() {return qty;}
}
