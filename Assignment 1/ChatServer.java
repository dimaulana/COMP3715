import java.io.*;
import java.net.*;

public class ChatServer 
{
    public static void main(String[] args) 
    {
    	boolean clientWaiting = false;
    	String $h = null;
    	String $p = null;

		try 
		{
		    ServerSocket server = new ServerSocket(0);
		    System.out.println("Server port is " + server.getLocalPort());

		    while(true) 
		    {
				Socket client = server.accept();

		        // data from client
				BufferedReader rd = new BufferedReader(new InputStreamReader(client.getInputStream()));
		        // data to client
		        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

				PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

				if(!clientWaiting)
				{
					writer.println("WAIT");
					$h = client.getInetAddress().getHostName();
					$p = rd.readLine();
					clientWaiting = true;
				}
				else
				{
					writer.println($h + " " + $p);
					clientWaiting = false;
				}

		        // print client details
				System.out.println("Accepted connection from " + client.getInetAddress() + " at port " + client.getPort());

		        System.out.println( "closing" + client);
		        // clean up
				client.close();
		    }
		}
		catch(IOException e) 
		{
		    System.out.println("error: " + e);
		}
    }
}