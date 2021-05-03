package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static java.lang.System.exit;

public class Server {
    Game game;
    boolean gameOver;
    boolean playerDisconnected;
    public Object lock = new Object();

    void waitForLock(){
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                notifyLock();
                playerDisconnected = true;
                System.out.println("inside the InterruptedException call in lock()");
                e.printStackTrace();
            } catch (NullPointerException e){
                notifyLock();
                playerDisconnected = true;
                System.out.println("inside the NullPointerException call in lock()");
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
        game = new Game();
        try {
            int initializePID = 0;
            Socket player2Conn, player1Conn;
            ServerSocket socket = new ServerSocket(7000);
            while(true) {
                Socket conn = socket.accept();
                playerDisconnected = false;
                System.out.println("client connected");
                initializePID++;
                if((initializePID % 2) == 0){
                    player2Conn = conn;
                    System.out.println("two players connected starting game...");
                }
                else if((initializePID % 2) == 1){
                    player1Conn = conn;
                }
                new Thread(new PlayerThread(conn, initializePID)).start();
            }
        } catch (SocketException e){
            playerDisconnected = true;
            System.out.println("inside the SocketException call in Server");
        } catch(NullPointerException e){
            playerDisconnected = true;
            System.out.println("inside the NullPointerException call in Server");
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
                            gameOver = true;
                            System.out.println("inside the playerDisconnected if statement in Thread " + playerID);
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
                        initialMessage.println("GUESS> ");
                        initialMessage.flush();
                        while (hit != 0 && !playerDisconnected) {
                            BufferedReader sharedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                            String guess = sharedReader.readLine();
                            PrintWriter writer = new PrintWriter(playerSocket.getOutputStream());
                            String rez = "Invalid input (incorrect guess format, guess has already been guessed, or guess is out of range) guess again> ";
                            if (playerID == 1) {
                                hit = guessResult(guess, true);
                            } else if (playerID == 2) {
                                hit = guessResult(guess, false);
                            }
                            if (hit == 1) {
                                rez = "You got a hit! guess again> ";
                            } else if (hit == 0) {
                                rez = "You missed. Opponent guessing...";
                            }
                            writer.println(rez);
                            writer.flush();
                            if (gameOver) {
                                hit = -2;
                                PrintWriter endOfGameWriter = new PrintWriter(playerSocket.getOutputStream());
                                endOfGameWriter.println("YOU WON");
                                endOfGameWriter.flush();
                                notifyLock();
                                break;
                            }
                        }
                        if (hit == 0 && !playerDisconnected) {
                            System.out.println("miss if statement for player" + playerID);
                            hit = 10;
                            notifyLock();
                            waitForLock();
                            if (gameOver) {
                                break;
                            }
                        }
                    } catch (NullPointerException e){
                        notifyLock();
                        playerDisconnected = true;
                        System.out.println("inside the NullPointerException call in Thread " + playerID);
                    } catch (SocketException e){
                        notifyLock();
                        playerDisconnected = true;
                        System.out.println("inside the SocketException call in Thread " + playerID);

                    } catch (IOException e) {
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


