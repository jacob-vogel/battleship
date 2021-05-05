package com.company;

import java.util.ArrayList;

public class Board {
    int gridPersonal[];
    int gridLength = 10;
    int gridSize = 100;
    ArrayList<Ship> ships;
    int shipCount;

    public Board(){
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
        int[] coords = new int[shipSize];
        int attempts = 0;
        boolean success = false;
        int location;

        shipCount++;
        int incr = 1;
        if((shipCount%2) ==1){
            incr = gridLength;
        }

        while(!success & attempts++ < 200){
            location = (int) (Math.random() * gridSize);
            int x = 0;
            success = true;
            while(success && x < shipSize){
                if(gridPersonal[location] == 0){
                    coords[x] = location;
                    x++;
                    location+=incr;
                    if(location >= gridSize || location < 0){
                        success = false;
                    }
                    int temp = location + shipSize;
                    int tempMod = temp % 10;
                    int locMod = location % 10;
                    if(locMod < tempMod){
                        success = false;
                    }
                }else{
                    success = false;
                }
            }
        }
        int x = 0;
        System.out.print("This is the location of ship with size: " + shipSize + " Location: ");
        for(int i = 0; i < shipSize; i++){
            System.out.print("coords[" + i + "] = " + coords[i] + " ");
        }
        System.out.print("\n");
        while(x<shipSize) {
            gridPersonal[coords[x]] = 1;
            x++;
        }
        return coords;
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
