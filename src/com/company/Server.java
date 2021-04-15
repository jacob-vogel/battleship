package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    Game game;
    int player1Port = 0;
    int player2Port = 0;
    BufferedReader sharedReader;
    int hit;
    String sharedString;
    Object lock = new Object();
    Socket conn;                //not sure if this works or is a good idea

    void waitForLock(){
        try{
            lock.wait();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    void notifyLock(){
        synchronized (lock){
            lock.notify();
        }
    }

    public static void main(String[] args){
        new Server().serverSetUp();
    }

    public void serverSetUp(){
        game = new Game();
        try {
            ServerSocket socket = new ServerSocket(10000);
            while(true) {
                conn = socket.accept();
                System.out.println("client connected");
                if(player1Port == 0) {
                    player1Port = conn.getPort();
                    new Thread(new Player1Thread()).start();
                }else if (player2Port == 0){
                    player2Port = conn.getPort();
                    new Thread(new Player2Thread()). start();
                }
                sharedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //int guessResult;
    public String guessResult(boolean p){
        String result = "something weird is happening with game";
        int player;
        if(p){ player = 1; }
        else{ player = 2; }
        if(game.guess(sharedString, p) == 1){
            hit = 1;
            result = String.format("Player %d got a hit at: %s", player, sharedString);
        }else if(game.guess(sharedString, p) == 0){
            hit = 0;
            result = String.format("Player %d missed at: %s", player, sharedString);
        }else if(game.guess(sharedString, p) == -1){
            hit = -1;
            result = String.format("Player %d entered an invalid command: %s", player, sharedString);
        }
        return result;
    }

    class Player1Thread implements Runnable{
        @Override
        public void run() {
            while(true){
                System.out.print("PLAYER 1> input: ");
                try{
                    sharedString = sharedReader.readLine();
                    PrintWriter writer = new PrintWriter(conn.getOutputStream());
                    writer.println(guessResult(true));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(hit == 0) {
                    notifyLock();
                    waitForLock();
                }
                System.out.println("PLAYER> from player 2: " + sharedString);
            }
        }
    }

    class Player2Thread implements Runnable{
        @Override
        public void run() {
            boolean first = true;
            while(true){
                if(first) {
                    waitForLock(); //i think this wait is only needed for the first time when it is player ones turn and player 2 waits, else it can have the same wait and notify as player 1
                    first = false;
                }
                System.out.println("PLAYER 2> from player 1: ");
                System.out.println("PLAYER 2> input: ");
                try{
                    sharedString = sharedReader.readLine();
                    PrintWriter writer = new PrintWriter(conn.getOutputStream());
                    writer.println(guessResult(false));
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(hit == 0) {
                    notifyLock();
                    waitForLock();
                }
            }
        }
    }
}


