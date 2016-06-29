package com.droid.app.fotobot;

/**
 * Created by voran on 6/29/16.
 */
public class Item {

    String animalName;
    int animalImage;

    public Item(String animalName,int animalImage)
    {
        this.animalImage=animalImage;
        this.animalName=animalName;
    }
    public String getAnimalName()
    {
        return animalName;
    }
    public int getAnimalImage()
    {
        return animalImage;
    }
}