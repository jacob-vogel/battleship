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

    public static void main(String[] args){
        Server server = new Server();
    }

    public void sleeper(int sleepTime){
        try {
            sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Server(){
        try {
            initializePID = 0;
            ServerSocket socket = new ServerSocket(7000);
            while(true) {
                if(initializePID < 2) {
                    System.out.println("Waiting for clients to connect...");
                }else{
                    do{
                        sleeper(15000);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PlayerThread implements Runnable{
        Socket playerSocket;
        int playerID;
        PrintWriter socketWriter;
        BufferedReader socketReader;

        public PlayerThread(Socket s1, int pid){
            playerSocket = s1;
            playerID = pid;
            socketWriter = null;
            try {
                socketWriter = new PrintWriter(playerSocket.getOutputStream());
                socketReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        public int guessResult(String guess, boolean p){
            int GR = game.guess(guess, p);
            if(GR == 1){
                return 1;
            }else if(GR == 0){
                return 0;
            }
            return -1;
        }

        public void exceptionEnder(){
            notifyLock();
            playerDisconnected = true;
            initializePID = 0;
        }

        @Override
        public void run() {
            int timeOut = 0;
            while(playerDisconnected && (timeOut < 100)){
                sleeper(1000);
                timeOut++;
            }
            if(timeOut >= 100){
                socketWriter.println("No opponent connected please try again later");
                initializePID = 0;
            } else{
                socketWriter.println("Successfully connected to other player!");
            }
            socketWriter.flush();
            if ((playerID % 2) == 0) {
                socketWriter.println("Opponent guessing...");
                socketWriter.flush();
                waitForLock();
            }
            gameOver = false;
            int hit = 10;
            while (!gameOver) {
                try {
                    if(playerDisconnected){
                        socketWriter.println("OPPONENT DISCONNECTED: YOU WON!");
                        socketWriter.flush();
                        break;
                    }
                    socketWriter.println("Your turn! Guess");
                    socketWriter.flush();
                    while (hit != 0 && !playerDisconnected) {
                        String guess = socketReader.readLine();
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
                        socketWriter.println(rez);
                        socketWriter.flush();
                        gameOver = game.isGameEnd();
                        if (gameOver) {
                            initializePID = 0;
                            hit = -2;
                            socketWriter.println("YOU WON");
                            socketWriter.flush();
                            notifyLock();
                            break;
                        }
                    }
                    if (hit == 0 && !playerDisconnected) {
                        hit = 10;
                        notifyLock();
                        waitForLock();
                        if (gameOver) {
                            break;
                        }
                    }
                } catch (SocketException e) {
                    exceptionEnder();
                } catch (NullPointerException e){
                    exceptionEnder();
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(!playerDisconnected) {
                socketWriter.println("GAME OVER: YOU LOST");
                socketWriter.flush();
            }
        }
    }
}


