/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleftp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author linroex
 */
public class Server {

    private final int port = 1234;
    private ServerSocket server;

    public Server() {
        try {
            this.server = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listen() {
        while (true) {
            try {
                Socket client = this.server.accept();

                System.out.println(client.getInetAddress() + " connected");

                Thread thread = new Thread(new ListenClientRunnable(client));
                thread.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private class ListenClientRunnable implements Runnable {

        private DataInputStream input;

        public ListenClientRunnable(Socket client) {
            try {
                this.input = new DataInputStream(client.getInputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String data = this.input.readUTF();

                    System.out.println(data);

                    // Identify command and do something.
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }
}
