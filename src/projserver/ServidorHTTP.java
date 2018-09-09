/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projserver;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author Carlos Ramalho
 */
public class ServidorHTTP implements Runnable {



    
    private String path;
    private Socket cliente;
    
    
    public ServidorHTTP(Socket c){
        this.cliente = c;
        this.path = System.getProperty("user.dir") + File.separator + "paginas";
        System.out.println("Cliente conectado-> InetAdrres:" + c.getInetAddress().toString() + " Host Name: " + c.getInetAddress().getHostName());
                      
    }
    
    
    
    
    @Override
    public void run() {
        BufferedReader input = null;
        PrintWriter output = null;
        DataOutputStream dataOut = null;
        
        try{
            input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            output = new PrintWriter(cliente.getOutputStream());
            dataOut = new DataOutputStream(cliente.getOutputStream());
            String requisicao = input.readLine();
            
            StringTokenizer strTok = new StringTokenizer(requisicao);
            System.out.println(requisicao);
            String metodo = strTok.nextToken().toUpperCase();
            String arquivo = strTok.nextToken().toLowerCase();
            
            
            
            if (metodo.equals("GET"))
            {
             
                File arq = new File(path + arquivo);
                int tamanhoArq = (int)arq.length();
                
                String tipoArq = "";
                if ( arq.getName().endsWith(".html") || (arq.getName().endsWith(".html")))
                    tipoArq = "text/html";                  
                else
                    tipoArq = "text/plain";
                
                byte[] fileData = readFile(arq, tamanhoArq);
                
                output.println("HTTP/1.1 200 OK");
		output.println("Server: HTTP Server teste : 1.0");
		output.println("Date: " + new Date());
		output.println("Content-type: " + tipoArq);
		output.println("Content-length: " + tamanhoArq);
		output.println(); 
		output.flush();
					
		dataOut.write(fileData, 0, tamanhoArq);
		dataOut.flush();                        
            }
            
            
        }catch(FileNotFoundException fnf){
            try{
                paginaNaoEncontrada(output, dataOut);
            }catch(IOException io){
                System.err.println("deu ruim:" + io.getMessage());                
            }
            
        }catch(IOException io){
            System.err.println("deu ruim:" + io.getMessage());             
        }finally{
            try{
                dataOut.close();
                input.close();
                output.close();
                cliente.close();
                
            }catch(Exception e){
                System.err.println("deu ruim:" + e.getMessage());
            }
           
            
        }
    }
    
    
    
    
    private byte[] readFile(File arq, int tam) throws IOException{
        
    		FileInputStream fileIn = null;
		byte[] fileData = new byte[tam];
		
		try {
			fileIn = new FileInputStream(arq);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
			    fileIn.close();
		}
		
		return fileData;    
    }
    
    
    private void paginaNaoEncontrada(PrintWriter output, DataOutputStream dataOut) throws IOException{
        
        File arq = new File(path + "//404.html");
        int tamanhoArq = (int)arq.length();
                
        String tipoArq = "text/html";
        
        byte[] fileData = readFile(arq, tamanhoArq);
        
        output.println("HTTP/1.1 404 File Not Found");
	output.println("Server: HTTP Server teste : 1.0");
	output.println("Date: " + new Date());
	output.println("Content-type: " + tipoArq);
	output.println("Content-length: " + tamanhoArq);
	output.println(); 
	output.flush();
					
	dataOut.write(fileData, 0, tamanhoArq);
	dataOut.flush(); 
    }
    
}
