package socket;

import java.io.*;
import java.net.*;

public class MainClass {
	public static void main( String[] args )
	{
		ServerSocket s = null;
		Socket conn = null;
        try {
            s = new ServerSocket(5000 , 10);
            echo("Server socket created.Waiting for connection...");
            while (true)
            {
                conn = s.accept();
                echo("Connection received from " + conn.getInetAddress().getHostName() + " : " + conn.getPort());
                new ClientHandler(conn).start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void echo(String s)
	{
		System.out.println(s);
	}
}

