package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class Controller {
    View view;
    Model model;
    String currentGuess;
    boolean confirmButtonPressed;

    public Controller(){
        view = new View(10);
        model = new Model(10);
        currentGuess = "-";
        confirmButtonPressed = false;
        ConfirmButtonActionListener confirmButtonActionListener = new ConfirmButtonActionListener();
        view.setConfirmButtonListener(confirmButtonActionListener);
    }

    public String getCurrentGuess(){
        confirmButtonPressed = false;
        return currentGuess;
    }

    public int convertGuess(){
        String guess = getCurrentGuess();
        int numVal = Character.getNumericValue(guess.charAt(1));
        if(guess.length() == 3){
            numVal = 10;
        }
        char letter = Character.toLowerCase(guess.charAt(0));
        int letterVal = (int) letter;
        letterVal-=97;
        return (letterVal*10)+(numVal-1);
    }

    public void enableViewButton(){
        view.enableButton();
    }

    public void changeViewBasedOnResult(String guessResult){
        int guess = convertGuess();
        if(guessResult.equals("Would you like to attempt to connect to another player? (yes/no) if not the game will exit")){
            //view.enableButton();
        }else if(guessResult.equals("Waiting for player to connect...")){
            view.disableButton();
        }
        /*else if(guessResult.equals("Successfully connected to other player!")){
            view.setResultLabel(guessResult);
        }else if(guessResult.equals("Opponent guessing...")){
            view.setResultLabel(guessResult);
        }*/
        else if (guessResult.equals("Your turn! Guess")){
                view.enableButton();
        }else if(guessResult.equals("You got a hit! Guess again") || guessResult.equals("GAME OVER: YOU LOST") || guessResult.equals("YOU WON")){
            view.changeGridAfterGuess(guess, 1);
        }
        else if(guessResult.equals("You missed. Opponent guessing...")){
            view.disableButton();
            view.changeGridAfterGuess(guess, 0);
        }
        view.setResultLabel(guessResult);
    }

    class ConfirmButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            currentGuess = view.getInputText();
            confirmButtonPressed = true;
        }
    }
}
