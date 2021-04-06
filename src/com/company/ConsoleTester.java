package com.company;

import java.util.Scanner;

public class ConsoleTester {

    public static void main(String[] args) {
        Game game = new Game();
        boolean gameEnd = false; //should we move the isEndGame function into game??
        boolean whichPlayer = game.getPlayer();
        while(!gameEnd){
            Scanner reader = new Scanner(System.in);
            boolean hit;
            if(whichPlayer == false){//need a getPlayer function in game???
                Scanner reader = new Scanner(System.in);
                System.out.println("PLAYER 1s TURN");
                hit = true;
                while(hit){
                    System.out.println("Guess a coordinate Player 1");
                    System.out.print("player1> ");
                    String guess = reader.nextLine();
                    int formatGuess[];
                    formatGuess = game.getGuessCord(guess, whichPlayer);
                    //game should have a Guess() function that calls a boards hitOrMiss() based on
                    //a passed in player boolean because I don't have access to the Boards
                    hit = game.guess(formatGuess, whichPlayer);
                    if(hit){
                        System.out.println("Player 1 got a hit!");
                        //Should we add a toString function to game that gives the current game status
                        //how does user know if they sank a ship for example
                        if(game.isGameEnd() == true){//need to add this isGameEnd function
                            gameEnd = true;
                            hit = false;
                        }
                    }
                }
                if(!gameEnd) {
                    System.out.println("Player 1 missed");
                    whichPlayer = true;
                }
            }
            else{
                System.out.println("PLAYER 2s TURN");
                hit = true;
                while(hit){
                    System.out.println("Guess a coordinate Player 2");
                    System.out.print("player2> ");
                    String guess = reader.nextLine();
                    int formatGuess[];
                    formatGuess = game.getGuessCord(guess, whichPlayer);
                    //game should have a Guess() function that calls a boards hitOrMiss() based on
                    //a passed in player boolean because I don't have access to the Boards
                    hit = game.guess(formatGuess, whichPlayer);
                    if(hit){
                        System.out.println("Player 2 got a hit!");
                        //Should we add a toString function to game that gives the current game status
                        //how does user know if they sank a ship for example
                        if(game.isGameEnd() == true){//need to add this isGameEnd function
                            gameEnd = true;
                            hit = false;
                        }
                    }
                }
                if(!gameEnd) {
                    System.out.println("Player 2 missed");
                    whichPlayer = true;
                }

            }
        }
        System.out.println("GAME OVER");
        //it would be nice to use that toString to show the final status of the game here as well
        //a winner or loser is unknown to me the programmer here
        System.out.println("Thanks for playing battleship");
    }
}
