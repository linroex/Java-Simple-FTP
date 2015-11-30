/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author linroex
 */
public class Client {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    
    private boolean loginFlag = false;
    private final int port = 1234;
    
    public Client(String ipAddress) {
        try {
            this.socket = new Socket(ipAddress, this.port);
            
            System.out.println("Connect success");
            
            this.input = new DataInputStream(this.socket.getInputStream());
            this.output = new DataOutputStream(this.socket.getOutputStream());
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    
    public void sendCommand(String command) {
        try {
            this.output.writeUTF(command);
            this.output.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void startInteractive() {
        final Scanner input = new Scanner(System.in);
        boolean interactiveFlag = true;
        
        while(interactiveFlag) {
            if(this.loginFlag == true) {
                String data = input.nextLine();
                
                if(data.trim().equals("logout")) {
                    this.sendCommand("logout ");

                    this.loginFlag = false;

                    System.out.println("Logout success");
                } else {
                    this.sendCommand(data);
                }
                
            } else {
                // Login
                System.out.print("login: ");
                String username = input.nextLine();
                
                System.out.print("please enter password: ");
                String password = input.nextLine();
                
                String loginStatus = this.login(username, password);
                
                if(loginStatus.equals("200")) {
                    this.loginFlag = true;
                    
                    System.out.println("Hello " + username + "! please enter command:");
                } else if(loginStatus.equals("300")) {
                    this.loginFlag = false;
                    
                    System.out.println("username or password error, please recheck");
                } else if(loginStatus.equals("100")) {
                    System.out.println("You are already login");
                }
            }
            
        }
    }
    
    private String login(String username, String password) {
        this.sendCommand("login " + username + " " + password);

        return this.receiveData();
    }
    
    private String receiveData() {
        try {
            return this.input.readUTF();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
}
