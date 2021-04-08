package com.company;

import java.util.Locale;

public class Ship {
    protected int size;
    //0 = destroyer(size 2) 1 = submarine(size 3) 2 = cruiser (size 3)
    //3 = battleship(size 4) 4 = carrier (size 5);
    protected int[] location;
    private boolean sunk;

    Ship(){
        sunk = false;
        size = 0;
        //type = -1;
    }

    public boolean checkYourself(int hit){
        for(int i = 0; i < size; i++){
            if(location[i] == hit){
                this.decrementSize();
                if(this.isSunk()){
                    sunk = true;
                }
                return true;
            }
        }
        return false;

    }

    public void setLocation(int[] coordinates){
        location = coordinates;
    }

    public int[] getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public boolean isSunk() {
        if(size <= 0){
            sunk = true;
        }
        else{
            sunk = false;
        }
        return sunk;
    }

    public void decrementSize(){
        size--;
    }
}
