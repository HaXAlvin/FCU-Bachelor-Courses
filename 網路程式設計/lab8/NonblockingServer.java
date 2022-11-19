//*******************************************************************
//*  Network Programming - Unit 8 Non-blocking Socket               *
//*  Program Name: NonblockingServer                                *
//*  The program creates a nonblocking mode socket.                 *
//*  2016.02.04                                                     *
//*******************************************************************
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class NonblockingServer
{
	public static void main(String args[])
	{
		ServerSocketChannel	ssc = null;
		ServerSocket		serverSocket = null;
		SocketChannel 		sc = null;
		int					port1 = 8880;
		int					port2 = 8881;
		int					loopcount =0;
		
		try
		{
		    // Create a server socket channel, bind to the specified port.
			ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false); // Config the channel to non-blocking mode
			serverSocket = ssc.socket();
			serverSocket.bind(new InetSocketAddress(port1));
			serverSocket.bind(new InetSocketAddress(port2));
			for(loopcount = 0 ; true ; loopcount++)
			{
				sc = ssc.accept(); 
				// if sc == null, there is no connection yet
				if(sc != null) // receive incoming connection
				{
					// Config the channel to non-blocking mode, then read()/write()
					// operations on this channel are also non-blocking mode.
					sc.configureBlocking(false); 

					// Receive message
					ByteBuffer 	b = ByteBuffer.allocate(100); 
					int			len = sc.read(b);	// read message from sc
					while(len == 0)	// return 0 if channel has no message
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

					// Send message to client
					String data = "Server hello!! (" + loopcount +")";
					ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
					sc.write(buffer);

					//Closes this socket
					sc.close();				
				}
				else // do something 
				{
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
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
}
