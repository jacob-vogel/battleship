package com.company;

public class Model {
    private int attackGrid[];
    int gridLength;
    int gridSize;
    public Model(int length){
        gridLength = length;
        gridSize = length * length;
        attackGrid = new int[gridSize];
    }
    public void addResult(int location, int result){
        if(result == 1){
            attackGrid[location] = result;
        }else{
            attackGrid[location] = result;
        }
    }
    public int[] getAttackGrid(){
        return attackGrid;
    }
}
