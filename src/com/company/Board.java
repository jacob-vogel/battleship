package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    protected int gridPersonal[];
    private int gridLength = 10;
    private int gridSize = 100;
    ArrayList<Ship> ships;
    private int shipCount;

    public Board(){
        gridPersonal = new int[100];
        ships = new ArrayList<Ship>();
        ships.add(new Submarine());
        ships.add(new Battleship());
        ships.add(new Cruiser());
        ships.add(new Carrier());
        ships.add(new Destroyer());

        for(Ship currentShip : ships){
            int[] coordinates = setShipPosition(currentShip);
            currentShip.setLocation(coordinates);
        }
    }

    int[] setShipPosition(Ship ship){
        int shipSize = ship.getSize();
        int[] shipCoordinates = new int[shipSize];
        int attempts = 0;
        boolean success = false;
        int location;
        shipCount++;
        int increment = 1;
        boolean decrement = true;
        if((shipCount%2) == 1){
            increment = gridLength;
            decrement = false;
        }
        while(!success & attempts++ < 200){
            Random random = new Random();
            location = random.nextInt(gridSize);
            int x = 0;
            success = true;
            while(success && x < shipSize){
                if(gridPersonal[location] == 0){
                    shipCoordinates[x] = location;
                    if(decrement){
                        x++;
                        location-=increment;
                        if(location >= gridSize || location < 0){
                            success = false;
                        }
                        int temp = location - shipSize;
                        int tempMod = temp % 10;
                        int locMod = location % 10;
                        if(locMod > tempMod){
                            success = false;
                        }
                    }else {
                        x++;
                        location += increment;
                        if (location >= gridSize || location < 0) {
                            success = false;
                        }
                        int temp = location + shipSize;
                        int tempMod = temp % 10;
                        int locMod = location % 10;
                        if (locMod < tempMod) {
                            success = false;
                        }
                    }
                }else{
                    success = false;
                }
            }
        }
        int x = 0;
        while(x<shipSize) {
            gridPersonal[shipCoordinates[x]] = 1;
            x++;
        }
        return shipCoordinates;
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
