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
//this is BS
class BS
{
	public static void main(String[] args)
		throws UnknownHostException, SocketException, java.io.IOException
	{
		int				port = 8766;
		String			addr = "224.0.0.2";
		InetAddress		group = InetAddress.getByName(addr);
		
		
		Socket			client = null;
		Socket			sc = null;
		InputStream		in = null;
		//OutputStream	out = null;
		int				TCPport = 6666;
		
		try
		{
			// Creates a stream socket and connects it to the specified port number 
			// at the specified IP address.
			client = new Socket("127.0.0.1", TCPport);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		
		
		
		// Create a multicast socket and bind it to a specific port
		MulticastSocket	mc_socket = new MulticastSocket(port);
		// Set to a non-zero timeout, a call to receive() for this DatagramSocket 
		// will block for only this amount of time
		// mc_socket.setSoTimeout(100); // only wait 1 second
		// Join a multicast group
		mc_socket.joinGroup(group);
		System.out.println("Join multicast group " + addr + " ......");
		
		// Prepare buffer for receiving messages
		byte []			data = new byte[1000];
		//DatagramPacket	packet = new DatagramPacket(data, data.length);
		
		for(int i = 0 ; true ; i++)
		{
			
			//Receive from BR
			in = client.getInputStream();
			in.read(data);
			System.out.println("Get BS: " + new String(data));
			
			
			DatagramPacket	packet = new DatagramPacket(data, data.length, group, port);
			System.out.println("Send BS: " + new String(data));
			mc_socket.send(packet);
			
		}
		/*
		System.out.println("Leave multicast group " + addr + " ......");
		mc_socket.leaveGroup(group);
		mc_socket.close();
		*/
	}
}