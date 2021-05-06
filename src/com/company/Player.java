package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class Player {
    private Socket comm;
    private boolean connected;
    private PrintWriter writer;
    private BufferedReader socketReader;
    private InputStreamReader stream;
    private Controller controller;

    public void waitForInput(int timeOut, String prompt){
        int i = 0;
        int j = 0;
        while(i < timeOut){
            if(controller.confirmButtonPressed){
                break;
            }
            sleeper(500);
            if((i == (timeOut) - 1) && j != 1){
                controller.changeViewBasedOnResult("Please respond, if not game will exit");
                sleeper(10000);
                controller.changeViewBasedOnResult(prompt);
                i = 0;
                j = 1;
            }else {
                i++;
            }
        }
        if(i >= timeOut){
            exit(0);
        }
    }

    public void sleeper(int sleepTime){
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResponse(int waitTime, String prompt){
        waitForInput(waitTime, prompt);
        String response = controller.getCurrentGuess();
        return (response.toLowerCase(Locale.ROOT));
    }

    public Player(){
        controller = new Controller();
        connected = false;
        boolean stay = true;
        controller.changeViewBasedOnResult("Welcome to Battleship!");
        sleeper(3000);
        controller.changeViewBasedOnResult("Would you like to attempt to connect to another player? (yes/no) if not the game will exit");
        String response = getResponse(200, "Would you like to attempt to connect to another player? (yes/no) if not the game will exit");
        if(response.equals("no") || response.equals("-")){
            stay = false;
        }
        while(stay) {
            this.attemptToConnect();
            if (connected) {
                boolean endGame = false;
                try {
                    controller.changeViewBasedOnResult("Waiting for another player to connect...");
                    String connectionMessage = socketReader.readLine();
                    controller.changeViewBasedOnResult(connectionMessage);
                    if(connectionMessage.equals("No opponent connected please try again later")){
                        sleeper(5000);
                        exit(0);
                    }
                    while (!endGame) {
                        String playerMsg = socketReader.readLine();
                        controller.changeViewBasedOnResult(playerMsg);
                        if (playerMsg.equals("Your turn! Guess")) {
                            boolean hit = true;
                            while (hit) {
                                response = getResponse(200, "Your turn! Guess");
                                writer.println(response);
                                writer.flush();
                                playerMsg = socketReader.readLine();
                                controller.changeViewBasedOnResult(playerMsg);
                                if (playerMsg.equals("You missed. Opponent guessing...")) {
                                    hit = false;
                                } else if (playerMsg.equals("GAME OVER: YOU LOST") || playerMsg.equals("YOU WON")) {
                                    sleeper(5000);
                                    endGame = true;
                                    hit = false;
                                    stay = false;
                                } else if (playerMsg.equals("Invalid input (incorrect format, already been guessed, or out of range)")){
                                    sleeper(4000);
                                    controller.changeViewBasedOnResult("Valid Input: {letter}{number}; Letter Range: a-j Number Range: 1-10. Guess Again.");
                                }
                            }
                        } else if(playerMsg.equals("OPPONENT DISCONNECTED: YOU WON!")){
                            sleeper(10000);
                            exit(0);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                controller.changeViewBasedOnResult("ERROR: Unable to connect to server");
                sleeper(5000);
                controller.changeViewBasedOnResult("Would you like to try again? (type 'yes' to stay)");
                response = getResponse(200, "Would you like to try again? (type 'yes' to stay)");
                if (response.equals("yes")) {
                    stay = true;
                } else {
                    stay = false;
                }
            }
        }
        controller.changeViewBasedOnResult("Thanks for Playing!");
        sleeper(5000);
        exit(0);
    }

    public void attemptToConnect(){
        controller.changeViewBasedOnResult("Attempting to connect to host with port 7000");
        try{
            comm = new Socket("127.0.0.1", 7000);
            writer = new PrintWriter(comm.getOutputStream());
            stream = new InputStreamReader(comm.getInputStream());
            socketReader = new BufferedReader(stream);
            writer.flush();
            connected = true;
        } catch (UnknownHostException e){
            connected = false;
        } catch (IOException e) {
            connected = false;
        }
    }
}
