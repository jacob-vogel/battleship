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
                try {
                    System.out.println("Connection Successful!");
                    System.out.println("Ships have been set START GAME!");
                    while (!endGame) {
                        String playerMsg = socketReader.readLine();
                        //System.out.print("playerMsg = " + playerMsg);
                        System.out.print(playerMsg + " ");
                        if(playerMsg.equals("Opponent guessing...")){
                            System.out.println(" ");
                        }
                        else if (playerMsg.equals("GUESS> ")) {
                            boolean hit = true;
                            while (hit) {
                                response = reader.nextLine();
                                writer.println(response);
                                writer.flush();
                                playerMsg = socketReader.readLine();
                                System.out.print(playerMsg);
                                if (playerMsg.equals("You missed. Opponent guessing...")) {
                                    System.out.println(" ");
                                    hit = false;
                                } else if (playerMsg.equals("GAME OVER: YOU LOST") || playerMsg.equals("YOU WON")) {
                                    System.out.println(" ");
                                    endGame = true;
                                    hit = false;
                                    stay = false;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ERROR: Unable to connect to server");
                System.out.println("Would you like to try again?");
                response = reader.nextLine();
                response = response.toLowerCase();
                //System.out.println(response);
                if (response.equals("yes")) {
                    stay = true;
                } else {
                    stay = false;
                }
            }
        }
        System.out.println("Thanks for Playing!");
    }

    public void attemptToConnect(){
        System.out.println("Attempting to connect to host with port 7000");
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
