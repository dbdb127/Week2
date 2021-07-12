package com.seungwoodev.project2;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Dictionary;

public class Item {
    // common
    public int type;
    public String text;
    public ImageView image;
    public ArrayList<Item> invisibleChildren;

    public Item(){

    }

    //if child
    public Item(int type, String text) {
        this.type = type;
        this.text = text;
    }

    //if header
    public Item(int type, String text, ImageView image) {
        this.type = type;
        this.text = text;
        this.image = image;
    }

    public String getText(){
        return text;
    }
}
