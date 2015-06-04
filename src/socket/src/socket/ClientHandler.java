package socket;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClientHandler extends Thread
{
	private Socket conn;
	private long processed = 0;
    
	ClientHandler(Socket conn)
    {
        this.conn = conn;
    }
    
	public void run()
	{
    	PrintStream out = null;
        try {
	        out = new PrintStream(conn.getOutputStream());
	        out.flush();
	        
	        Random rand = new Random();
	        int min = 1;
	        int max = 20;
	
	        while (!conn.isInputShutdown()) 
	        {
	        	int i = rand.nextInt((max - min) + 1) + min;
	        	int j = rand.nextInt((max - min) + 1) + min;          
	        	if (i != j) 
	        	{
	        		String tuple = i + "," + j;
	        		processed++;
	        		out.println(tuple);
	        	}
	        	if (processed > 0 && processed % 1000000 == 0)
	        	{
	        		System.out.println("Clicks procesados en este hilo: " + processed/1000000 + " millones");
	        	}
	        }
	        
	        try {
	            out.close();
	            conn.close();
	        	System.out.println("Connection closed " + conn.getInetAddress().getHostName() + " : " + conn.getPort());
	        } catch(IOException ioException){
	            System.err.println("Unable to close. IOexception");
	            ioException.printStackTrace();
	        }
        } catch(IOException e) {
        	System.err.println("Connection error. IOexception");
            e.printStackTrace();
        }
	}
}
