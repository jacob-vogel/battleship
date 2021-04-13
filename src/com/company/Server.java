package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    Game game;
    int player1Port = 0;
    int player2Port = 0;
    BufferedReader sharedReader;
    String sharedString;
    Object lock = new Object();
// wait and notify for server stuff

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
                Socket conn = socket.accept();
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

    class Player1Thread implements Runnable{
        @Override
        public void run() {
            while(true){
                System.out.print("PLAYER 1> input: ");
                try{
                    sharedString = sharedReader.readLine();
                    game.guess(sharedString, true);
                }catch (Exception e){

                    e.printStackTrace();
                }
                notifyLock();
                waitForLock();
                System.out.println("PLAYER> from player 2: " + sharedString);
            }
        }
    }

    class Player2Thread implements Runnable{
        @Override
        public void run() {
            while(true){
                while(true){
                    waitForLock();
                    System.out.println("PLAYER 2> from player 1: ");
                    System.out.println("PLAYER 2> input: ");
                    try{
                        sharedString = sharedReader.readLine();
                        game.guess(sharedString, false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    notifyLock();
                }
            }
        }
    }
}


