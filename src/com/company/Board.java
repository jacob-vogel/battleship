package com.company;

import java.util.ArrayList;

public class Board {
    private static final String alphabet = "abcdefghij";
    //int gridAttacks[];
    int gridPersonal[];
    int gridLength = 10;
    int gridSize = 100;
    ArrayList<Ship> ships; //how are we going to keep track of ship location
    int shipCount;

    public Board(){
        //gridAttacks = new int[100];
        gridPersonal = new int[100];
        ships = new ArrayList<Ship>();
        ships.add(new Submarine());
        ships.add(new Battleship());
        ships.add(new Cruiser());
        ships.add(new Carrier());
        ships.add(new Destroyer());

        for(Ship curShip : ships){
            int[] coordinates = setShipPos(curShip);
            curShip.setLocation(coordinates);
        }
    }

    int[] setShipPos(Ship ship){
        int shipSize = ship.getSize();
        System.out.println("this is size in setShipPos = " + shipSize);
        //ArrayList<String> alphaCells = new ArrayList<String>();
        //String temp = null;
        int[] coords = new int[shipSize];
        int attempts = 0;
        boolean success = false;
        int location;

        shipCount++;
        int incr = 1;
        if((shipCount%2) ==1){//probably make more random
            incr = gridLength;
        }

        while(!success & attempts++ < 200){
            location = (int) (Math.random() * gridSize);
            int x = 0;
            success = true;
            while(success && x < shipSize){
                if(gridPersonal[location] == 0){
                    coords[x++] = location;
                    location+=incr;
                    if(location >= gridSize){
                        success = false;
                    }
                }else{
                    success = false;
                }
            }
        }
        int x = 0;
        while(x<shipSize){
            gridPersonal[coords[x]] = 1;
            x++;
        }
        return coords;
        //call set ships location
    }
    boolean hitOrMiss(int guess){
        if(gridPersonal[guess] == 1){
            gridPersonal[guess] = 2;
            for(Ship shipCheck : ships){
                if(shipCheck.checkYourself(guess)){
                    return true;
                }
            }
        }
        gridPersonal[guess] = 3;
        return false;
    }
    boolean allShipsGone(){
        for(Ship sunkShip : ships){
            if(!sunkShip.isSunk()){
                return false;
            }
        }
        return true;
    }
}
// hello
// hey
 /* for(int i = 0; i < 5; i++){
                Ship temp = ships.get(i);
                int size = temp.getSize();
                int SorD = (int) Math.random() * 1; // 0 for sideways, 1 for down
                int x;
                int y;
                if(SorD == 0){
                    while(true) {
                        x = (int) Math.random() * 9 - size;
                        y = (int) Math.random() * 9;
                        boolean valid = true;
                        for (int j = x; j <= (x + size); j++) {
                            if (gridPersonal[x][y] == 1) {
                                // make x new random or not
                                valid = false;
                                break;
                            }
                        }
                        if(valid){
                            break;
                        }
                    }
                    for(int c = x; )
                }else{
                    y = (int) Math.random() * 9 - size;
                    x = (int) Math.random() * 9;
                    for(int j = y; j <= (y+size); j++){
                        if(gridPersonal[x][y] == 1){
                            // make x new random or not
                        }
                    }
                }
                Math.random();
            }*/
