package com.company;

public class Ship {
    protected int size;
    protected int permSize;
    protected int[] location;
    private boolean sunk;

    Ship(){
        sunk = false;
        size = 0;
    }

    public boolean checkYourself(int hit){
        for(int i = 0; i < permSize; i++){
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
