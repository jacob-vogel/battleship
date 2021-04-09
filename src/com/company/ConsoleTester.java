package com.company;

import java.util.Scanner;

public class ConsoleTester {

    public static void main(String[] args) {
        Game game = new Game();
        boolean gameEnd = false; //should we move the isEndGame function into game??
        boolean whichPlayer = true;
        boolean winner = true;
        while(!gameEnd){
            //Scanner reader = new Scanner(System.in);
            int hit = 1;
            if(whichPlayer == true){//need a getPlayer function in game???
                Scanner reader1 = new Scanner(System.in);
                System.out.println("-----------PLAYER 1s TURN----------");
                while(hit == 1){
                    System.out.println("player1> Guess a coordinate {Row Letter}{Column Number}");
                    System.out.print("player1> ");
                    String guess = reader1.nextLine();
                    //int formatGuess[];
                    //formatGuess = game.getGuessCord(guess, whichPlayer);
                    //game should have a Guess() function that calls a boards hitOrMiss() based on
                    //a passed in player boolean because I don't have access to the Boards
                    hit = game.guess(guess, whichPlayer);
                    if(hit == 1){
                        System.out.println("player1> You got a hit!");
                        //Should we add a toString function to game that gives the current game status
                        //how does user know if they sank a ship for example
                        if(game.isGameEnd() == true){//need to add this isGameEnd function
                            gameEnd = true;
                            hit = -2;
                        }
                    }
                    if(hit == -1){
                        System.out.println("player1> Invalid guess, please guess again {Row Letter}{Column Number}");
                        hit = 1;
                    }
                }
                if(!gameEnd) {
                    System.out.println("player1> You missed!");
                    whichPlayer = false;
                }
            }
            else{//need a getPlayer function in game???
                //CHANGE THIS SO IT IS PLAYER2
                Scanner reader2 = new Scanner(System.in);
                System.out.println("-----------PLAYER 2s TURN----------");
                hit = 1;
                while(hit == 1){
                    System.out.println("player2> Guess a coordinate{Row Letter}{Column Number}");
                    System.out.print("player2> ");
                    String guess = reader2.nextLine();
                    //int formatGuess[];
                    //formatGuess = game.getGuessCord(guess, whichPlayer);
                    //game should have a Guess() function that calls a boards hitOrMiss() based on
                    //a passed in player boolean because I don't have access to the Boards
                    hit = game.guess(guess, whichPlayer);
                    if(hit == 1){
                        System.out.println("player2> You got a hit!");
                        //Should we add a toString function to game that gives the current game status
                        //how does user know if they sank a ship for example
                        if(game.isGameEnd() == true){//need to add this isGameEnd function
                            gameEnd = true;
                            hit = -2;
                            winner = false;
                        }
                    }
                    else if(hit == -1){
                        System.out.println("player2> Invalid guess, please guess again {Row Letter}{Column Number}");
                        hit = 1;
                    }
                }
                if(!gameEnd) {
                    System.out.println("player2> You missed!");
                    whichPlayer = true;
                }
            }
        }
        if(winner){
            System.out.println("PLAYER 1 WINS");
        }
        else{
            System.out.println("PLAYER 2 WINS");
        }
        System.out.println("GAME OVER");
        //it would be nice to use that toString to show the final status of the game here as well
        //a winner or loser is unknown to me the programmer here
        System.out.println("Thanks for playing battleship");
    }
}
