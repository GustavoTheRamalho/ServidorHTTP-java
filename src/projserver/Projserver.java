/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projserver;

import java.net.ServerSocket;

/**
 *
 * @author Carlos Ramalho
 */
public class Projserver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         try{
           ServerSocket server = null;
           ServidorHTTP serverHTTP = null;
       
            server = new ServerSocket(8080);
            System.out.println("Aguardando conexoes...");
                        
       
            while(1 != 0){
                serverHTTP = new ServidorHTTP(server.accept());
                Thread thread = new Thread(serverHTTP);
                thread.start();
            }
        
        
        
        }catch(Exception err){
            
        }
  
        
    }
    
}
