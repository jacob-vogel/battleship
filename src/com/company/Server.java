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
    int hit = 10;
    String sharedString;
    public Object lock = new Object();
    public boolean first = true;
                   //not sure if this works or is a good idea

    void waitForLock(){
        synchronized (lock) {
            try {
                lock.wait();
                System.out.println("locked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void notifyLock(){
        synchronized (lock){
            System.out.println("unlocked");
            lock.notify();
        }
    }

    public static void main(String[] args){
        new Server().serverSetUp();
    }

    public void serverSetUp(){
        game = new Game();
        try {
            int initializePID = 0;
            ServerSocket socket = new ServerSocket(5000);
            while(true) {
                Socket conn = socket.accept();
                System.out.println("client connected");
                initializePID++;// will this get incremented? or should it be above the thread creation and player 1 has PID 1 and player 2 has PID 2
                new Thread(new PlayerThread(conn, initializePID)).start();
                //initializePID++;// will this get incremented? or should it be above the thread creation and player 1 has PID 1 and player 2 has PID 2
                /*
                if(player1Port == 0) {
                    player1Port = conn.getPort();
                    new Thread(new Player1Thread()).start();
                }else if (player2Port == 0){
                    player2Port = conn.getPort();
                    new Thread(new Player2Thread()). start();
                }
                 */
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //int guessResult;
    public String guessResult(boolean p){
        String result = "something weird is happening with game";
        /*int player;
        if(p){ player = 1; }
        else{ player = 2; }*/
        int GR = game.guess(sharedString, p);
        if(GR == 1){
            hit = 1;
            result = "You got a hit, guess again.";//String.format("Player %d got a hit at: %s", player, sharedString);
        }else if(GR == 0){
            hit = 0;
            result = "You missed.";//String.format("Player %d missed at: %s", player, sharedString);
        }else if(GR == -1){
            hit = -1;
            result = "Invalid input, guess again.";//String.format("Player %d entered an invalid command: %s", player, sharedString);
        }
        return result;
    }

    class PlayerThread implements Runnable{
        Socket playerSocket;
        int playerID;
        public PlayerThread(Socket s1, int pid){
            playerSocket = s1;
            playerID = pid;
            System.out.println("player id is: " + playerID);
        }
        @Override
        public void run() {
            if(playerID == 2 && first){
                first = false;
                System.out.println("player 2 locked intialize");
                waitForLock(); //i think this wait is only needed for the first time when it is player ones turn and player 2 waits, else it can have the same wait and notify as player 1
            }
            while(true){
                try{
                    PrintWriter initialMessage = new PrintWriter(playerSocket.getOutputStream());
                    initialMessage.println("GUESS> ");
                    System.out.println("GUESS>");
                    initialMessage.flush();
                    while(hit != 0) {
                        BufferedReader sharedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));;
                        sharedString = sharedReader.readLine();
                        PrintWriter writer = new PrintWriter(playerSocket.getOutputStream());
                        String rez = "if this was not changed then playerID was not set correctly";
                        if (playerID == 1) {
                            rez = guessResult(true);
                        } else if (playerID == 2) {
                            rez = guessResult(false);
                        }
                        System.out.println(rez);
                        writer.print(rez);
                        writer.flush();
                    }
                    if(hit == 0) {
                        notifyLock();
                        waitForLock();
                    }
                    //System.out.println("PLAYER> from player 2: " + sharedString);
                    if(game.isGameEnd()){
                        PrintWriter endOfGameWriter = new PrintWriter(playerSocket.getOutputStream());
                        endOfGameWriter.print("GAME OVER");//(String.format("GAME OVER: player %d won", playerID+1));
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
/*
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

 */
}


