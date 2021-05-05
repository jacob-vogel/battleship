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

    public void waitForInput(int timeOut){
        int i = 0;
        int j = 0;
        while(i < timeOut){
            try {
                //response = controller.getCurrentGuess();
                if(controller.confirmButtonPressed){
                    break;
                }
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if((i == (timeOut) - 1) && j != 1){
                controller.changeViewBasedOnResult("Please respond, if not game will exit");
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

    public Player(){
        controller = new Controller();
        connected = false;
        boolean stay = true;
        System.out.println("Welcome to Battleship!");//display on gui
        controller.changeViewBasedOnResult("Welcome to Battleship!");
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Would you like to attempt to connect to another player? (yes/no) if not the game will exit");//display on gui
        controller.changeViewBasedOnResult("Would you like to attempt to connect to another player? (yes/no) if not the game will exit");
        //Scanner reader = new Scanner(System.in);
        waitForInput(200);
        String response = controller.getCurrentGuess();
        response = response.toLowerCase(Locale.ROOT);
        System.out.println("this is response when we wait: " + response);
        if(response.equals("no") || response.equals("-")){
            stay = false;
        }
        while(stay) {
            this.attemptToConnect();
            if (connected) {
                boolean endGame = false;
                try {
                    System.out.println("Connection to Server Successful!");
                    controller.changeViewBasedOnResult("Waiting for another player to connect...");//gui
                    //System.out.println("...");
                    String connectionMessage = socketReader.readLine();
                    controller.changeViewBasedOnResult(connectionMessage);
                    if(connectionMessage.equals("Successfully connected to other player!")){//gui
                        //System.out.println(" ");
                        //System.out.println("Ships have been set START GAME!");
                    }
                    else{
                        exit(0);
                    }
                    while (!endGame) {
                        String playerMsg = socketReader.readLine();
                        System.out.print(playerMsg + " ");
                        controller.changeViewBasedOnResult(playerMsg);
                        if(playerMsg.equals("Opponent guessing...")){
                            System.out.println(" ");
                        }
                        else if (playerMsg.equals("Your turn! Guess")) {
                            boolean hit = true;
                            while (hit) {
                                waitForInput(200);
                                response = controller.getCurrentGuess();
                                writer.println(response);
                                writer.flush();
                                playerMsg = socketReader.readLine();
                                controller.changeViewBasedOnResult(playerMsg);
                                if (playerMsg.equals("You missed. Opponent guessing...")) {
                                    System.out.println(" ");
                                    hit = false;
                                } else if (playerMsg.equals("GAME OVER: YOU LOST") || playerMsg.equals("YOU WON")) {
                                    System.out.println(playerMsg);
                                    try {
                                        sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    endGame = true;
                                    hit = false;
                                    stay = false;
                                }  else if(playerMsg.equals("Invalid input (incorrect format, already been guessed, or out of range)")){
                                    System.out.println(" ");
                                    System.out.println("Valid input(No spaces): {Letter a-j}{Number 1-10}");
                                    System.out.print("GUESS> ");
                                }
                            }
                        } else if(playerMsg.equals("OPPONENT DISCONNECTED: YOU WON!")){
                            try {
                                sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            exit(0);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                controller.changeViewBasedOnResult("ERROR: Unable to connect to server");
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                controller.changeViewBasedOnResult("Would you like to try again? (type 'yes' to stay)");
                waitForInput(20);
                response = controller.getCurrentGuess();
                response = response.toLowerCase();
                if (response.equals("yes")) {
                    stay = true;
                } else {
                    stay = false;
                }
            }
        }
        controller.changeViewBasedOnResult("Thanks for Playing!");
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
