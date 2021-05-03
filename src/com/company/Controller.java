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

    public Controller(){
        view = new View(10);
        model = new Model(10);
        ConfirmButtonActionListener confirmButtonActionListener = new ConfirmButtonActionListener();
        view.setConfirmButtonListener(confirmButtonActionListener);
    }

    public String getCurrentGuess(){
        return currentGuess;
    }

    public int convertGuess(){
        String guess = getCurrentGuess();
        int numVal = Character.getNumericValue(guess.charAt(1));
        if(guess.length() == 3){
            numVal = 10;
        }
        char letter = Character.toLowerCase(guess.charAt(0));
        int letterVal = Character.getNumericValue(letter);
        letterVal-=97;
        return (letterVal*10)+(numVal-1);
    }

    public void enableViewButton(){
        view.enableButton();
    }

    public void changeViewBasedOnResult(String guessResult){
        int guess = convertGuess();
        if(guessResult.equals("You missed. Opponent guessing...")){
            //change grid at currentGuess to gray
            view.changeGridAfterGuess(guess, 0);
            //display this the string guessResult on resultLabel
            view.disableButton();
            view.setResultLabel(guessResult);
            return;
        }else if(guessResult.equals("GAME OVER: YOU LOST") || guessResult.equals("YOU WON")){
            //change grid at current guess
            //display this the string guessResult on resultLabel
            view.changeGridAfterGuess(guess, 1);
            view.setResultLabel(guessResult);
            //freeze gui or terminate game
            return;
        }
        //else if(guessResult.equals("You got a hit. Guess again.")){
            //change grid at current guess to red
            //display this string guessResult on result label
            view.changeGridAfterGuess(guess, 1);
            view.setResultLabel("You got a hit. Guess again.");
        //}
    }

    class ConfirmButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            currentGuess = view.getInputText();
        }
    }

    public class MyWindowListener implements WindowListener {
        @Override
        public void windowClosing(WindowEvent e) {
                System.exit(0);
        }
        @Override
        public void windowOpened(WindowEvent e) {}
        @Override
        public void windowClosed(WindowEvent e) {}
        @Override
        public void windowIconified(WindowEvent e) {}
        @Override
        public void windowDeiconified(WindowEvent e) {}
        @Override
        public void windowActivated(WindowEvent e) {}
        @Override
        public void windowDeactivated(WindowEvent e) {}
    }

}
