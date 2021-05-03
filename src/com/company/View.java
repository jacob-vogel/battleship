package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {
    JFrame frame;

    JButton grid[][];// = new JButton[10][10];
    JLabel enterPromptLabel;
    JTextField enterCommandTextField;
    JButton confirmButton;
    //JButton resultPrompt;
    JLabel resultPromptLable;
    JLabel resultLabel;

    public View(int length){
        frame = new JFrame();
        grid = new JButton[length+1][length+1];
        enterPromptLabel = new JLabel("Enter Coordinate: ");
        enterCommandTextField = new JTextField(80);
        confirmButton = new JButton("Confirm Guess");
        resultPromptLable = new JLabel("Result: ");
        resultLabel = new JLabel();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(length+1, length+1, 10, 10));
        int ascii = 97;//97 = a in ascii
        for(int rows = 0; rows <= length; rows++){
            for(int columns = 0; columns <= length; columns++){
                grid[rows][columns] = new JButton();
                grid[rows][columns].setPreferredSize(new Dimension(10, 50));
                if(rows == 0 && columns == 0){
                    grid[rows][columns].setText("BS");
                    grid[rows][columns].setBackground(Color.WHITE);
                }else if(rows == 0 && columns != 0){
                    grid[rows][columns].setText(String.valueOf(columns));
                }else if(columns == 0 && rows != 0){
                    grid[rows][columns].setText(String.valueOf((char)ascii));
                    ascii+=1;
                }else{
                    grid[rows][columns].setBackground(Color.BLUE);
                }
                gridPanel.add(grid[rows][columns]);
            }
        }

        JPanel userInteractionPanel = new JPanel();
        userInteractionPanel.add(enterPromptLabel);
        userInteractionPanel.add(enterCommandTextField);
        userInteractionPanel.add(confirmButton);

        JPanel userGuessResultPanel = new JPanel();
        userGuessResultPanel.add(resultPromptLable);
        userGuessResultPanel.add(resultLabel);

        frame.getContentPane().add(BorderLayout.NORTH, gridPanel);
        frame.getContentPane().add(BorderLayout.CENTER, userInteractionPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, userGuessResultPanel);

        frame.setSize(300, 500);
        frame.setVisible(true);
    }
    public void setConfirmButtonListener(ActionListener actionListener){confirmButton.addActionListener(actionListener);}

    public String getInputText(){return enterCommandTextField.getText();}

    public void setResultLabel(String result){
        resultLabel.setText(result);
    }

    public void disableButton(){
        confirmButton.setEnabled(false);
    }

    public void enableButton(){
        confirmButton.setEnabled(true);
    }

    public void changeGridAfterGuess(int guess, int result){
        int numberColumn = (guess % 10)+1;
        int letterRow = ((guess - numberColumn)/10)+1;
        if(result == 1){
            grid[letterRow][numberColumn].setBackground(Color.RED);
        }else if (result == 0){
            grid[letterRow][numberColumn].setBackground(Color.GRAY);
        }
    }
    //public static void main(String[] args){
      //  new View(10);
    //}
}
