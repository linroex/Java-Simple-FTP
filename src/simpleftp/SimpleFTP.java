/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleftp;

/**
 *
 * @author linroex
 */
public class SimpleFTP {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        switch(args[0]) {
            case "server":
                Server server = new Server();
                server.listen();
                break;
            case "client":
                Client client = new Client(args[1]);
                client.sendCommand("hello");
                break;
                    
        }
    }
}
