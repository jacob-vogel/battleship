package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import static java.lang.Thread.sleep;

public class Server {
    Game game;
    boolean gameOver;
    boolean playerDisconnected;
    public Object lock = new Object();
    int initializePID;

    void waitForLock(){
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        try {
            initializePID = 0;
            ServerSocket socket = new ServerSocket(7000);
            while(true) {
                if(initializePID < 2) {
                    System.out.println("Waiting for clients to connect...");
                }else{
                    do{
                        sleep(10000);
                    }while(initializePID != 0);
                    System.out.println("Waiting for clients to connect...");
                }
                Socket conn = socket.accept();
                playerDisconnected = true;
                System.out.println("client connected");
                initializePID++;
                if((initializePID % 2) == 0){
                    game = new Game();
                    playerDisconnected = false;
                    System.out.println("two players connected starting game...");
                    System.out.println(" ");
                }
                new Thread(new PlayerThread(conn, initializePID)).start();
            }
        } catch (SocketException e) {
            playerDisconnected = true;
            initializePID = 0;
            notifyLock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PlayerThread implements Runnable{
        Socket playerSocket;
        int playerID;
        public PlayerThread(Socket s1, int pid){
            playerSocket = s1;
            playerID = pid;
        }

        public int guessResult(String guess, boolean p){
            int GR = game.guess(guess, p);
            if(GR == 1){
                return 1;
            }else if(GR == 0){
                return 0;
            }
            return -1;
        }

        @Override
        public void run() {
            int timeOut = 0;
            while(playerDisconnected && (timeOut < 100)){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeOut++;
            }
            PrintWriter connectionMessage = null;
            try {
                connectionMessage = new PrintWriter(playerSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(timeOut >= 100){
                connectionMessage.println("No opponent connected please try again later");//gui
                initializePID = 0;
            } else{
                connectionMessage.println("Successfully connected to other player!");//gui
            }
            connectionMessage.flush();
            if ((playerID % 2) == 0) {
                PrintWriter player2Message = null;
                try {
                    player2Message = new PrintWriter(playerSocket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player2Message.println("Opponent guessing...");
                player2Message.flush();
                waitForLock();
            }
            gameOver = false;
            int hit = 10;
            while (!gameOver) {
                try {
                    if(playerDisconnected){
                        //System.out.println("Inside playerDisconnected if statement in player " + playerID);
                        PrintWriter endOfGameWriter = null;
                        try {
                            endOfGameWriter = new PrintWriter(playerSocket.getOutputStream());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        endOfGameWriter.println("OPPONENT DISCONNECTED: YOU WON!");
                        endOfGameWriter.flush();
                        break;
                    }
                    PrintWriter initialMessage = new PrintWriter(playerSocket.getOutputStream());
                    initialMessage.println("Your turn! Guess");
                    initialMessage.flush();
                    while (hit != 0 && !playerDisconnected) {
                        BufferedReader sharedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                        String guess = sharedReader.readLine();
                        PrintWriter writer = new PrintWriter(playerSocket.getOutputStream());
                        String rez = "Invalid input (incorrect format, already been guessed, or out of range)";
                        if (playerID == 1) {
                            hit = guessResult(guess, true);
                        } else if (playerID == 2) {
                            hit = guessResult(guess, false);
                        }
                        if (hit == 1) {
                            rez = "You got a hit! Guess again";
                        } else if (hit == 0) {
                            rez = "You missed. Opponent guessing...";
                        }
                        writer.println(rez);
                        writer.flush();
                        gameOver = game.isGameEnd();
                        if (gameOver) {
                            System.out.println("its the end");
                            initializePID = 0;
                            hit = -2;
                            PrintWriter endOfGameWriter = new PrintWriter(playerSocket.getOutputStream());
                            endOfGameWriter.println("YOU WON");
                            endOfGameWriter.flush();
                            notifyLock();
                            break;
                        }
                    }
                    if (hit == 0 && !playerDisconnected) {
                        //System.out.println("inside miss statement this is playerDisconnected = " + playerDisconnected + " player" + playerID);
                        hit = 10;
                        notifyLock();
                        waitForLock();
                        //System.out.println("after wait for lock in miss statement this is playerDisconnected = " + playerDisconnected + " player" + playerID);
                        if (gameOver) {
                            break;
                        }
                    }
                    //System.out.println("after miss statement player" + playerID);
                } catch (SocketException e) {
                    //System.out.println("SocketException hit in thread" + playerID + " unlocking...");
                    notifyLock();
                    playerDisconnected = true;
                    initializePID = 0;
                } catch (NullPointerException e){
                    //System.out.println("Nullpointerexception hit in thread" + playerID + " unlocking...");
                    notifyLock();
                    playerDisconnected = true;
                    initializePID = 0;
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(!playerDisconnected) {
                PrintWriter finalMessageWriter = null;
                try {
                    finalMessageWriter = new PrintWriter(playerSocket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finalMessageWriter.println("GAME OVER: YOU LOST");
                finalMessageWriter.flush();
            }
        }
    }
}


