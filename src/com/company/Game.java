package com.company;

public class Game {
    protected Board player1Board;
    protected Board player2Board;

    public Game(){
        player1Board = new Board();
        player2Board = new Board();
    }

    public boolean validGuess(int guess, boolean player){
        if(guess < 0 || guess > 99){
            return false;
        }
        if(!player) {
            if (player1Board.gridPersonal[guess] == 3 || player1Board.gridPersonal[guess] == 2) {
                return false;
            }
        }
        else {
            if (player2Board.gridPersonal[guess] == 3 || player2Board.gridPersonal[guess] == 2) {
                return false;
            }
        }
        return true;
    }

    int getGuessCord(String userGuess){
        int guess;
        int numberValue;
        if(userGuess.length() < 2 || userGuess.length() > 3){
            return -1;
        }
        if(userGuess.length() == 3){
            if(((Character.getNumericValue(userGuess.charAt(2)) == 0)) && (Character.getNumericValue(userGuess.charAt(1)) == 1)){
                numberValue = 10;
            }
            else{
                return -1;
            }
        }
        else{
            numberValue = Character.getNumericValue(userGuess.charAt(1));
        }
        if(numberValue < 1 || numberValue > 10){
            return -1;
        }
        numberValue -= 1;
        int characterValue;
        char letter = Character.toLowerCase(userGuess.charAt(0));
        if(letter < 97 || letter > 106){
            return -1;
        }
        else{
            characterValue = ((int) letter) - 97;
        }
        guess = (characterValue*10) + numberValue;
        return guess;
    }

    int guess(String userGuess, boolean player){
        int guess = getGuessCord(userGuess);
        if (!validGuess(guess, player)) {
            return -1;
        }
        if(player && player2Board.hitOrMiss(guess)){
            player2Board.gridPersonal[guess] = 2;
            return 1;
        }else if(player){
            player2Board.gridPersonal[guess] = 3;
            return 0;
        }
        if(!player && player1Board.hitOrMiss(guess)){
            player1Board.gridPersonal[guess] = 2;
            return 1;
        }else if(!player){
            player1Board.gridPersonal[guess] = 3;
            return 0;
        }
        return -1;
    }

    boolean isGameEnd(){
        if(player1Board.allShipsGone() || player2Board.allShipsGone()){
            return true;
        }
        return false;
    }
}