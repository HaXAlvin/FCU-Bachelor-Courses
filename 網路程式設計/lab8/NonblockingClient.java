//*******************************************************************
//*  Network Programming - Unit 8 Non-blocking Socket               *
//*  Program Name: NonblockingClient                                *
//*  The program creates a nonblocking mode socket.                 *
//*  The program connects to server and send/receive message.       *
//*  The program gets the server IP from args[0].                   *
//*  The message to send is arg[1].                                 *
//*  2016.02.04                                                     *
//*******************************************************************
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.net.*;
import java.io.*;

class NonblockingClient
{
	public static void main(String args[])
	{
		SocketChannel 	sc = null;
		int				port = 6666;
		int				loopcount = 0;

		if(args.length == 0)
		{
		    System.out.println("Usage: java SimpleClient server_ip");
		}
		else
		{
			try
			{
			    // Creates a socket channel 
			    sc = SocketChannel.open();
			    sc.configureBlocking(false);
			    sc.connect(new InetSocketAddress(args[0], port));
			    
            	// if the socket is connected, sc.finishConnect() returns false
            	for (loopcount = 0 ; !sc.finishConnect() ; loopcount++)
            	{
                	// do something 
                	System.out.println("Loop count = " + loopcount);
                	try 
                	{
                    	Thread.sleep(1000);
                	} 
                	catch (InterruptedException e) 
                	{
                    	System.err.println(e);
                	}
            	}
				// Connection established
									    
				// Send message to server
				if(args.length == 2)
				{
					ByteBuffer buffer = ByteBuffer.wrap(args[1].getBytes());
					sc.write(buffer);
				}
				else
				{
					String data = "Hello form client!!";
					ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
					sc.write(buffer);
				}

				// Receive message
				ByteBuffer 	b = ByteBuffer.allocate(100); 
				int			len = sc.read(b);	// read message from sc (non-blocking)
				while(len == 0)
				{
                	try 
                	{
                    	Thread.sleep(100);
                	} 
                	catch (InterruptedException e) 
                	{
                    	System.err.println(e);
                	}
					System.out.print("+"); // do something
				    len = sc.read(b);
				}
				System.out.println("Receive message : "	+ new String(b.array()));

				sc.close();
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