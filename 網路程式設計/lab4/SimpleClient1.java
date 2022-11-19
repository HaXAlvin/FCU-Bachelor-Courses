//*******************************************************************
//*  Network Programming - Unit 2 Simple Client and Server          *
//*  Program Name: SimpleClient1                                     *
//*  The program connects to server and send/receive message.       *
//*  The program gets the server IP from args[0].                   *
//*  2017.08.04
//*  Student D0745378
//*  Class 
//*******************************************************************
import java.net.*;
import java.io.*;

class SimpleClient1
{
	public static void main(String[] args)
	{
		Socket			client = null;
		InputStream 	in = null;
		OutputStream 	out = null;
		int				port = 6666;
		
		if(args.length < 2)
		{
		    System.out.println("Usage: java SimpleClient1 server_ip number");
		}
		else
		{
			try
			{
				String nowCount = args[1];
				client = new Socket(args[0], port);
				while(!nowCount.equals("0")){
					// Send message to server
					out = client.getOutputStream();

					out.write(nowCount.getBytes());
					
					// Read message from server
					in = client.getInputStream();
					byte [] buf = new byte[100];

					in.read(buf);
					nowCount = new String(buf).trim();
					System.out.println("Receive message: " + nowCount);
					if(nowCount.equals("0"))
						continue;
					int counter = Integer.parseInt(nowCount) - 1;
					nowCount = String.valueOf(counter);
					if(nowCount.equals("0")){
						out = client.getOutputStream();
						out.write(nowCount.getBytes());
					}
					
				}
				out.close();
				in.close();
				client.close();
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}