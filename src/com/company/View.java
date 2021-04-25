package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {
    JFrame frame;

    JLabel grid[][];// = new JButton[10][10];
    JLabel enterPromptLabel;
    JTextField enterCommandTextField;
    JButton confirmButton;
    //JButton resultPrompt;
    JLabel resultPromptLable;
    JLabel resultLabel;

    public View(int length){
        frame = new JFrame();
        //grid = new JButton[length][length];
        enterPromptLabel = new JLabel("Enter Coordinate: ");
        enterCommandTextField = new JTextField(80);
        confirmButton = new JButton("Confirm Guess");
        resultPromptLable = new JLabel("Result: ");
        resultLabel = new JLabel();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(length+1, length+1, 10, 10));
        for(int rows = 0; rows <= length+1; rows++){
            for(int columns = 0; columns <= length+1; columns++){
                int ascii = 97;//97 = a in ascii
                grid[rows][columns] = new JLabel();
                grid[rows][columns].setPreferredSize(new Dimension(100, 100));
                if(rows == 0 && columns == 0){
                    grid[rows][columns].setText("BS");
                }if(rows == 0){
                    grid[rows][columns].setText(String.valueOf(rows+1));
                }else if(columns == 0){
                    grid[rows][columns].setText(String.valueOf((char)ascii));
                    ascii++;
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
    }
    public void setConfirmButtonListener(ActionListener actionListener){confirmButton.addActionListener(actionListener);}
}
