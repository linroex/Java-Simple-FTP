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
        }
    }
    
    public void sendCommand(String command) {
        try {
            this.output.writeUTF(command);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void startInteractive() {
        final Scanner input = new Scanner(System.in);
        
        while(true) {
            if(this.loginFlag == true) {
                this.sendCommand(input.nextLine());
            } else {
                // Login
                System.out.print("login: ");
                String username = input.nextLine();
                
                System.out.print("please enter password: ");
                String password = input.nextLine();
                
                if(this.login(username, password)) {
                    this.loginFlag = true;
                } else {
                    System.out.println("username or password error, please recheck");
                }
            }
            
        }
    }
    
    private boolean login(String username, String password) {
        this.sendCommand("login " + username + " " + password);
        return Boolean.parseBoolean(this.receiveData());
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
