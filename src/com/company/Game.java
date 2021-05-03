package com.company;

public class Game {
    Board player1Board;
    Board player2Board;
    //boolean player;//true for player 1, false for player 2

    public Game(){
        player1Board = new Board();
        player2Board = new Board();
        //player = true;
    }

    public boolean validGuess(int guess, boolean player){
        System.out.println("this is guess: " + guess);
        if(guess < 0 || guess > 99){
            System.out.println("the coordinate given is not a coordinate");
            return false;
        }
        if(!player) {
            if (player1Board.gridPersonal[guess] == 3 || player1Board.gridPersonal[guess] == 2) {
                System.out.println("the coordinate given has already been guessed");
                return false;
            }
        }
        else{
            if (player2Board.gridPersonal[guess] == 3 || player2Board.gridPersonal[guess] == 2) {
                System.out.println("the coordinate given has already been guessed");
                return false;
            }
        }
        return true;
    }

    int getGuessCord(String userGuess){
        int guess;
        int numVal;
        if(userGuess.length() < 2 || userGuess.length() > 3){
            return -1;
        }
            if(userGuess.length() == 3){
                if(((Character.getNumericValue(userGuess.charAt(2)) == 0)) && (Character.getNumericValue(userGuess.charAt(1)) == 1)){
                    numVal = 10;
                }
                else{
                    return -1;
                }
            }
            else{
                numVal = Character.getNumericValue(userGuess.charAt(1));
            }
            if(numVal <= 1 || numVal > 10){
                return -1;
            }
        numVal -= 1;
        int charVal = -1;
        char letter = Character.toLowerCase(userGuess.charAt(0));
        //
        //int letterValue = letter;
        System.out.println("This is letterValue in get guess cord: " + letter);
        if(letter < 97 || letter > 105){return -1;}
        //
        char[] letterTrans = new char[10];
        letterTrans[0] = 'a';
        letterTrans[1] = 'b';
        letterTrans[2] = 'c';
        letterTrans[3] = 'd';
        letterTrans[4] = 'e';
        letterTrans[5] = 'f';
        letterTrans[6] = 'g';
        letterTrans[7] = 'h';
        letterTrans[8] = 'i';
        letterTrans[9] = 'j';
        for(int i = 0; i < 10; i++){
            if(letter == letterTrans[i]){
                charVal = i;
                //System.out.println("this is charVal in getGuessCord = " + charVal);
            }
        }
        if(charVal == -1){
            return -1;
        }
        guess = (charVal*10) + numVal;
        //System.out.println("this is guess in getGuessCord = " + guess);
        return guess;
    }

    int guess(String userGuess, boolean player){// -1 invalid, 0 for miss, 1 for hit ||||| player 1 for true, player 2 false
        int guess = getGuessCord(userGuess);
        if (!validGuess(guess, player)) {
            //System.out.println("Guess was invalid, guess again");
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
