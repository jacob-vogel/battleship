package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player {
    private Socket comm;
    private boolean connected;
    private boolean whichPlayer;
    private PrintWriter writer;
    private BufferedReader socketReader;
    private InputStreamReader stream;

    public Player(boolean playerNum){
        connected = false;
        whichPlayer = playerNum;
        this.attemptToConnect();
        if(connected){
            try {
                String playerMsg = socketReader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{

        }
    }

    public void attemptToConnect(){
        System.out.println("Attempting to connect to host with port 5000");
        try{
            comm = new Socket(127.0.0.1, 5000);
            writer = new PrintWriter(comm.getOutputStream());
            stream = new InputStreamReader(comm.getInputStream());
            socketReader = new BufferedReader(stream);
            writer.flush();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        } catch (UnknownHostException e){
            e.printStackTrace();
            connected = false;
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isWhichPlayer() {
        return whichPlayer;
    }
}
