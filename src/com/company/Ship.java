package com.company;

import java.util.Locale;

public class Ship {
    protected int size;
    //private int type;
    //0 = destroyer(size 2) 1 = submarine(size 3) 2 = cruiser (size 3)
    //3 = battleship(size 4) 4 = carrier (size 5);
    protected int[] location;
    private boolean sunk;

    Ship(){
        sunk = false;
        size = 0;
        //type = -1;
    }
    /*
    Ship(int ntype){
        sunk = false;
        type = ntype;
        if(ntype > 4 || ntype < 0){
            type = -1;
        }
        else if(ntype == 0){
            size = 2;
        }
        else if(ntype == 1){
            size = 3;
        }
        else if(ntype == 2){
            size = 3;
        }
        else if(ntype == 3){
            size = 4;
        }
        else if(ntype == 4){
            size = 5;
        }
    }
    */

    public void setLocation(int[] coordinates){
        location = coordinates;
    }

    public int[] getLocation() {
        return location;
    }

    /*
    public void setType(int ntype) {
        type = ntype;
        if(ntype > 4 || ntype < 0){
            type = -1;
        }
        else if(ntype == 0){
            size = 2;
        }
        else if(ntype == 1){
            size = 3;
        }
        else if(ntype == 2){
            size = 3;
        }
        else if(ntype == 3){
            size = 4;
        }
        else if(ntype == 4){
            size = 5;
        }
    }
    */

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

    /*
    public int getType() {
        return type;
    }
    */

    public void decrementSize(){
        size--;
    }
}
