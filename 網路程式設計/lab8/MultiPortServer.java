//*******************************************************************
//*  Network Programming - Unit 8 Non-blocking Socket               *
//*  Program Name: MultiPortServer                                  *
//*  The program creates a server that registers on multiple ports. *
//*     Usage: java MultiPortServer port1 [port2 port3 ...]       *
//*  2016.02.04                                                     *
//*******************************************************************
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiPortServer
{
	public static void main(String[] args) throws Exception
	{
		Selector 	selector = Selector.open();   	// Create a selector
		String[]	queueString = new String[5];
		int			queueFull = 0;
    	if (args.length <= 0)
    	{
			System.err.println("Usage: java MultiPortServer port1 [port2 port3 ...]");
			System.exit(1);
		}
		
		// Create non-blocking sockets on each port 
		// and register each socket channel with selector
		for (String arg : args) {
			int port = Integer.parseInt(arg);    // we don't handle the format error

			// Create a server socket channel and bind it to the specified port
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false); // Config the channel to non-blocking mode
			ServerSocket ss = ssc.socket();
			ss.bind(new InetSocketAddress(port));

			// Register the socket channel with selector
			// and listen on the SOCKET-ACCEPT event
			SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Listening on " + port + " port...");
		}
		
		// The main loop waiting for events
		while(true)
		{
		    // The following method blocks until 
		    // at least one of the registered events occurs.
			int num = selector.select();
			
			// Returns a Set of the SelectionKey objects 
			// for which events have occurred
			Set selectedKeys = selector.selectedKeys();
			// Organize the elements in set into iterator
			Iterator element = selectedKeys.iterator();
			
			
			while(element.hasNext()) // Handle each event in the set
			{
				// Returns the next element in the iteration.
				
				SelectionKey key = (SelectionKey)element.next();
				//System.out.println("Now OP = "+ key.readyOps());
				do{
					if(key.isAcceptable()) // Accept the new connection
					{
						// Return the ServerSocketChannel
						ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
						SocketChannel sc = ssc.accept();
						sc.configureBlocking(false);
						SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
						System.out.println("Got connection from " + sc);

					}
					else if(key.isReadable())
					{
						// Return the SocketChannel
						SocketChannel sc = (SocketChannel)key.channel();
						String toPort = sc.getLocalAddress().toString().substring(11);
						//System.out.println("Read Data");
						try
						{
							// Receive message
							ByteBuffer 	b = ByteBuffer.allocate(1000);
							String 		data;
							int			len = sc.read(b);	// read message from sc
							if(len <= 0) continue;
							String		recS = new String(b.array(), 0, len);
							System.out.println("Receive message : "	+ recS + " from " + toPort);
							if(toPort.equals("8880")) {//8880
								data = queueFull >= 5 ? "full" : "ok";
								ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
								sc.write(buffer);
								if (queueFull>=5) continue;
								queueString[queueFull++] = recS;
							}else if(toPort.equals("8881")) {///8881
								data = queueFull <= 0 ? "empty" : queueString[0];
								ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
								sc.write(buffer);
								if(queueFull <= 0) continue;
								System.arraycopy(queueString, 1, queueString, 0, --queueFull);
							}
							sc.close();

						}
						catch(IOException e)
						{
							System.out.println("Connection reset by peer :" + sc);
							sc.close();
						}
					}
				}while (false);
				// Remove the element from the iteration
				element.remove();
			} // end of handling each event
		} // end of main loop
	}
}
