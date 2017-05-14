package socket;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClientHandler extends Thread
{
	private Socket conn;
	private long processed = 0;
	private static int MIN = 1;
	private static int MAX = 100;
    
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
	        
	        long timeStart = System.currentTimeMillis();
	        long timeEnd;
	        Random rand = new Random();
	        while (!conn.isInputShutdown()) 
	        {
	        	int i = rand.nextInt((MAX - MIN) + 1) + MIN;
	        	int j = rand.nextInt((MAX - MIN) + 1) + MIN;          
	        	if (i != j) 
	        	{
	        		generateEvent(out, rand, i, j);
	        		processed++;
	        	}
	        	if (processed > 0 && processed % 100000 == 0)
	        	{
	        		timeEnd = System.currentTimeMillis();
	        		System.out.println("Eventos procesados en este hilo: " + (processed/1000000f) + "M");
	        		System.out.println("Últimos 100k tomaron: " + ((timeEnd - timeStart)/1000f) + " segundos");
	        		timeStart = timeEnd;
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
        	System.err.println("Connection error");
            e.printStackTrace();
		}
	}
	
	private void generateEvent(PrintStream out, Random rand, int i, int j)
	{
		String tuple = "";
		int r = rand.nextInt(10000);
		if (r % 67 == 0)
		{
			tuple = generateClick(i, j);
		} 
		else if (r <= 3)
		{
			tuple = generateInstall(i, j);
		}
		else
		{
			tuple = generatePrint(i, j);
		}
		out.println(tuple);
	}
	
	private String generatePrint(int i, int j)
	{
		return "p," + i + "," + j;
	}
	
	private String generateClick(int i, int j)
	{
		return "c," + i + "," + j;
	}
	
	private String generateInstall(int i, int j)
	{
		return "i," + i + "," + j;
	}
}
