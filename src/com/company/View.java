package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
        resultLabel.setFont(new Font("Serif", Font.BOLD, 20));

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
                    grid[rows][columns].setOpaque(true);
                }else if(rows == 0 && columns != 0){
                    grid[rows][columns].setText(String.valueOf(columns));
                }else if(columns == 0 && rows != 0){
                    grid[rows][columns].setText(String.valueOf((char)ascii));
                    ascii+=1;
                }else{
                    grid[rows][columns].setBackground(Color.BLUE);
                    grid[rows][columns].setOpaque(true);
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

        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addWindowListener(new MyWindowListener());
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
        int numberColumn = (guess % 10);
        int letterRow = ((guess - numberColumn)/10)+1;
        numberColumn++;
        if(result == 1){
            grid[letterRow][numberColumn].setBackground(Color.RED);
            grid[letterRow][numberColumn].setOpaque(true);
            grid[letterRow][numberColumn].setText("GODDEEEM");
        }else if (result == 0){
            grid[letterRow][numberColumn].setBackground(Color.GRAY);
            grid[letterRow][numberColumn].setOpaque(true);
            grid[letterRow][numberColumn].setText("SPLASH");
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

    public static void main(String[] args){
        new View(10);
    }
}
