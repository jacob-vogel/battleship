package com.company;

import java.util.Locale;

public class Ship {
    private int size;
    private String type;
    //0 = destroyer(size 2) 1 = submarine(size 3) 2 = cruiser (size 3)
    //3 = battleship(size 4) 4 = carrier (5 holes);
    private boolean sunk;

    Ship(){
        sunk = false;
        size = 0;
        type = "unset";
    }
    Ship(String ntype){
        String lowerType = ntype.toLowerCase(Locale.ROOT);
        boolean shipSet = false;
        sunk = false;
        if(lowerType.equals("destroyer")){
            type = "destroyer";
            size = 2;
        }
        else if(lowerType.equals("submarine")){
            type = "submarine";
            size = 3;
        }
        else if(lowerType.equals("cruiser")){
            type = "cruiser";
            size = 3;
        }
        else if(lowerType.equals("battleship")){
            type = "battleship";
            size = 4;
        }
        else if(lowerType.equals("carrier")){
            type = "carrier";
            size = 5;
        }
        else if(shipSet == false){
            System.out.println("Unable to set ship type in Ship(String ntype)");
        }
    }

    public void setType(String ntype) {
        String lowerType = ntype.toLowerCase(Locale.ROOT);
        boolean shipSet = false;
        if(lowerType.equals("destroyer")){
            type = "destroyer";
            size = 2;
        }
        else if(lowerType.equals("submarine")){
            type = "submarine";
            size = 3;
        }
        else if(lowerType.equals("cruiser")){
            type = "cruiser";
            size = 3;
        }
        else if(lowerType.equals("battleship")){
            type = "battleship";
            size = 4;
        }
        else if(lowerType.equals("carrier")){
            type = "carrier";
            size = 5;
        }
        else if(shipSet == false){
            System.out.println("Unable to set ship type in Ship(String ntype) to " + ntype);
            System.out.println("Ship type set to - 'unset'");
            type = "unset";
        }
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

    public String getType() {
        return type;
    }

    public void decrementSize(){
        size--;
    }
}
