//*******************************************************************
//*  Network Programming - Unit 9 Multicast                        *
//*  Program Name: MulticastReceiver                                *
//*  The program receives messages from a multicast group.          *
//*  2014.05.09                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;
//S->BR->BS->C
//MC  TCP  MC
//this is BR
class BR
{
	public static void main(String[] args)
		throws UnknownHostException, SocketException, java.io.IOException
	{
		int				port = 8765;
		String			addr = "224.0.0.1";
		InetAddress		group = InetAddress.getByName(addr);
		
		
		ServerSocket	srverSocket = null;
		Socket			sc = null;
		//InputStream		in = null;
		OutputStream	out = null;
		int				TCPport = 6666;
		
		try
		{
		    // Creates a server socket, bound to the specified port.
			srverSocket = new ServerSocket(TCPport);
			
			System.out.println("Waiting for request ...");
			try
			{
			    // Listens for a connection to be made to this socket and accepts it.
				sc = srverSocket.accept();
			}
			catch(IOException e)
			{
				System.err.println(e);
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
		
		
		
		
		// Create a multicast socket and bind it to a specific port
		MulticastSocket	mc_socket = new MulticastSocket(port);
		// Set to a non-zero timeout, a call to receive() for this DatagramSocket 
		// will block for only this amount of time
		mc_socket.setSoTimeout(100); // only wait 1 second
		// Join a multicast group
		mc_socket.joinGroup(group);
		System.out.println("Join multicast group " + addr + " ......");
		
		// Prepare buffer for receiving messages
		byte []			data = new byte[1000];
		DatagramPacket	packet = new DatagramPacket(data, data.length);
		
		for(int i = 0 ; true ; i++)
		{
			try
			{
				//Receive from S
				//System.out.println("Wait for receiving ...");
				mc_socket.receive(packet);
				String	inMsg = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Get  BR: " + inMsg);
				out = sc.getOutputStream();
				out.write(inMsg.getBytes());
				System.out.println("Send BR: " + inMsg);
				
			}
			catch(SocketTimeoutException e)
			{
				//System.out.println("Retry");
			}
		}
		/*
		System.out.println("Leave multicast group " + addr + " ......");
		mc_socket.leaveGroup(group);
		mc_socket.close();
		*/
	}
}