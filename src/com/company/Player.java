package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Scanner;

public class Player {
    private Socket comm;
    private boolean connected;
    //private boolean whichPlayer;// true -> player1 false -> player2
    private PrintWriter writer;
    private BufferedReader socketReader;
    private InputStreamReader stream;

    public Player(){
        connected = false;
        boolean stay = true;
        System.out.println("Welcome to Battleship!");
        System.out.println("Would you like to attempt to connect to another player? (yes/no) if not the game will exit");
        Scanner reader = new Scanner(System.in);
        String response = reader.nextLine();
        response = response.toLowerCase(Locale.ROOT);
        if(response.equals("no")){
            stay = false;
        }
        while(stay) {
            this.attemptToConnect();
            if (connected) {
                boolean endGame = false;
                while(!endGame) {
                    try {
                        System.out.println("Connection Successful!");
                        System.out.println("Ships have been set START GAME!");
                        String playerMsg = socketReader.readLine();
                        System.out.println(playerMsg);
                        if(playerMsg.equals("GUESS> ")){
                            boolean hit = true;
                            while(hit) {
                                response = reader.nextLine();
                                writer.println(response);
                                writer.flush();
                                playerMsg = socketReader.readLine();
                                System.out.println(playerMsg);
                                if (playerMsg.equals("You missed.")) {
                                    hit = false;
                                }
                                else if(playerMsg.equals("GAME OVER")){
                                    endGame = true;
                                    hit = false;
                                }
                            }
                        }
                        if (playerMsg.equals("endGame")) {
                            endGame = true;
                        }
                        //maybe check to see if a response is warranted don't want to always respond
                /*
                STEPS FOR CLIENT SERVER INTERACTION (sketch)
                1. user starts game via startBattleship class
                    1a. startBattleship creates a new Player() class with constructor
                2. Player() constructor
                    2a. player attempts to connect to server
                3. Successful connection
                    3a. player is assigned number (player 1 or 2) by server
                    3b. player sets its whichPlayer value
                    3c. server sends "guess?" / "endGame" / "otherPlayer"
                        3ci. if guess then get guess from console
                        3cii. if endGame then exit program
                        3ciii. if otherPlayer notify user that it is otherPlayer's turn
                4. Unsuccessful connection
                    4a. display unsuccessful connection to user
                    4b. ask if they would like to connect again or exit
                    4c. if exit then exit program
                    4d. if not exit
                 */
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Thanks for playing Battleship!");
            } else {
                System.out.println("ERROR: Unable to connect to server");
                System.out.println("Would you like to try again?");
                response = reader.nextLine();
                response = response.toLowerCase();
                //System.out.println(response);
                if(response.equals("yes")){
                    stay = true;
                }else {
                    stay = false;
                }
            }
        }
        System.out.println("Thanks for Playing!");
    }

    public void attemptToConnect(){
        System.out.println("Attempting to connect to host with port 5000");
        try{
            comm = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(comm.getOutputStream());
            stream = new InputStreamReader(comm.getInputStream());
            socketReader = new BufferedReader(stream);
            writer.flush();
            connected = true;
        } catch (UnknownHostException e){
            //e.printStackTrace();
            connected = false;
        } catch (IOException e) {
            //e.printStackTrace();
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
