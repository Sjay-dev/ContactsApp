package com.example.sigh;

public class Contact {

    private String name;
    private String number;
    private int imageResource;

    public void setName (String name){
        this.name = name;
    }

    public void setImageResource ( int imageResource){
        this.imageResource = imageResource;
    }

    public void setNumber (String number){
        this.number = number;
    }

    public String getName () {
        return name;
    }

    public boolean getNameSearched () {
        this.name = getName();
        return true;
    }

    public String getNumber () {
        return number;
    }

    public int getImageResource () {
        return imageResource;
    }

}

