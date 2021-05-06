package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private View view;
    private String currentGuess;
    protected boolean confirmButtonPressed;

    public Controller(){
        view = new View(10);
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

    public void changeViewBasedOnResult(String guessResult){
        view.setResultLabel(guessResult);
        int guess;
        if(guessResult.equals("Waiting for player to connect...")){
            view.disableButton();
        }else if (guessResult.equals("Your turn! Guess")){
            view.enableButton();
        }else if(guessResult.equals("You got a hit! Guess again") || guessResult.equals("GAME OVER: YOU LOST") || guessResult.equals("YOU WON")){
            guess = convertGuess();
            view.changeGridAfterGuess(guess, 1);
        }
        else if(guessResult.equals("You missed. Opponent guessing...")){
            guess = convertGuess();
            view.disableButton();
            view.changeGridAfterGuess(guess, 0);
        }
    }

    class ConfirmButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            currentGuess = view.getInputText();
            confirmButtonPressed = true;
        }
    }
}
