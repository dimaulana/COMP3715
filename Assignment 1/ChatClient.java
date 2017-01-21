import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
 * The client program can send text lines to a server program.
 */
public class ChatClient 
{

    public static void main(String[] args) 
    {
    	boolean proceed = true;

        if (args.length != 2) 
        {
		    System.out.println("usage: java ChatClient host port");
		    System.exit(1);
        }

		int port = 0;
		String host = null;
		
		try 
		{
		    host = args[0];
		    port = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e) 
		{
		    System.out.println("bad port number");
		    System.exit(1);
		}

		do
		{
	        try 
	        {
			    // determine the address of the server and connect to it
			    InetAddress address = InetAddress.getByName(host);
			    Socket server = new Socket(address, port);
			    
			    PrintWriter writer = new PrintWriter(server.getOutputStream(), true);

		    	BufferedReader b = new BufferedReader(new InputStreamReader(server.getInputStream()));

			    String status = b.readLine();

			    if(status.equals("WAIT"))
			    {
			    	System.out.println("Waiting for another client to pair with...");
			    	ServerSocket listen = new ServerSocket(0);
			    	writer.println(listen.getLocalPort());
			    	Socket client = listen.accept();
			    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			    	BufferedReader bin = new BufferedReader(new InputStreamReader(client.getInputStream()));

			    	PrintWriter writer2 = new PrintWriter(client.getOutputStream(), true);
			    	
			    	// print client details
					System.out.println("Accepted connection from " + client.getInetAddress() + " at port " + client.getPort());
				    
					String incoming = null;
					String outgoing = null;

					while(true)
					{
						incoming = bin.readLine();
						if(incoming.equals("exit"))	break;
				     	System.out.println("Houston: " + incoming);
				     	System.out.print("Apollo 1: ");
				     	outgoing = br.readLine();
				     	writer2.println(outgoing);
				     	if(outgoing.equals("exit"))
				     	{
				     		proceed = false;
				     		break;
				     	}
				    }

				    System.out.println("Closing connection!");
					// terminate the connection
					client.close();
			    }
			    else
			    {
			    	String[] details = status.split(" ");

			    	InetAddress peer = InetAddress.getByName(details[0]);
			    	int peer_port = Integer.parseInt(details[1]);

			    	server = new Socket(peer,peer_port);
			    	// attach it to a print writer
			    	PrintWriter writer3 = new PrintWriter(server.getOutputStream(), true);
			    	BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
		    		BufferedReader bin2 = new BufferedReader(new InputStreamReader(server.getInputStream()));

			    	// print client details
					System.out.println("Accepted connection from " + server.getInetAddress() + " at port " + server.getPort());

			    	String incoming = null;
			    	String outgoing = null;

					while(true)
					{
						System.out.print("Houston: ");
						outgoing = br2.readLine();
						writer3.println(outgoing);
						if(outgoing.equals("exit"))
						{
							proceed = false;
							break;
						}
				     	incoming = bin2.readLine();
				     	if(incoming.equals("exit"))	break;
				     	System.out.println("Apollo 1: " + incoming);
				    }

				    System.out.println("Closing connection!");
					// terminate the connection
					server.close();
			    }
			}
			catch(UnknownHostException e) 
			{
			    System.out.println("bad host name");
			    System.exit(0);
			}
			catch(IOException e) 
			{
			    System.out.println("io error:" + e);
			    System.exit(0);
			}
		}
		while(proceed);
    }
}