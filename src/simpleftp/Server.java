/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.File;
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
    
    private boolean clientLogin(String username, String password) {
        if(username.equals("linroex") == true && password.equals("123456") == true) {
            return true;
        } else {
            return false;
        }
    }

    private class ListenClientRunnable implements Runnable {
        private boolean connectFlag = true;
        private DataInputStream input;
        private DataOutputStream output;
        private final Socket client;
        private String username;
        private boolean loginStatus;

        public ListenClientRunnable(Socket client) {
            this.client = client;
            
            try {
                this.input = new DataInputStream(this.client.getInputStream());
                this.output = new DataOutputStream(this.client.getOutputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (this.connectFlag) {
                try {
                    String data = this.input.readUTF();
                    String[] columns = data.split(" ");
                    
                    System.out.println("input: " + data);
                    
                    // detect command type
                    switch(columns[0]) {
                        case "login":
                            
                            if(!this.loginStatus) {
                                this.loginStatus = clientLogin(columns[1], columns[2]);

                                if (this.loginStatus) {
                                    output.writeUTF("200");     //login success
                                    System.out.println(columns[1] + " login");
                                    
                                    this.username = columns[1];
                                } else {
                                    output.writeUTF("300");     //login failed
                                }
                            } else {
                                output.writeUTF("100");     // already login
                            }
                            
                            break;
                        case "logout":
                            System.out.println(this.username + " logout");
                            
                            this.username = "";
                            this.loginStatus = false;
                            
                            break;
                        case "list":
                            String fileList = "";
                            
                            for(File f_ : new File(columns[1]).listFiles()) {
                               fileList += f_.getName() + "\n";
                            }
                            
                            this.output.writeUTF(fileList);
                            
                            break;
                        case "get":
                            break;
                        case "del":
                            File file = new File(columns[1]);
                            
                            if(file.exists()) {
                                file.delete();
                                this.output.writeUTF("Delete " + file.toString() + " success");
                            } else {
                                this.output.writeUTF("File doesn't exist");
                            }
                            
                            break;
                        case "put":
                            break;
                    }
                    
                    output.flush();

                } catch (IOException e) {
                    if(e.getMessage() == null) {
                        this.connectFlag = false;
                        System.out.println("Client break");
                    }
                    
                }

            }
        }
    }
}
