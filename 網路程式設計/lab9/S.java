//*******************************************************************
//*  Network Programming - Unit 9 Multicast                        *
//*  Program Name: MulticastSender                                  *
//*  The program sends messages to a multicast group.               *
//*  2014.05.09                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;
//S->BR->BS->C
//MC  TCP  MC
//this is S
class S
{
	public static void main(String[] args)
		throws UnknownHostException, SocketException, java.io.IOException
	{
		int				port = 8765;
		String			addr = "224.0.0.1";
		InetAddress		group = InetAddress.getByName(addr);
		
		// Create a multicast socket and bind it to a specific port
		MulticastSocket	mc_socket = new MulticastSocket(port);
		// Join a multicast group
		mc_socket.joinGroup(group);
		System.out.println("Join multicast group " + addr + " ......");
		
		for(int i = 1 ; true ; i++)
		{
			String 			outMsg = "Multicast message (" + i + ")";
			byte []			data = outMsg.getBytes();
			// Construct a datagram packet for sending packets of length to 
			// the specified port number on the specified host (group).
			// Multicast uses UDP 
			DatagramPacket	packet = new DatagramPacket(data, data.length, group, port);
			System.out.println("Send S: " + outMsg);
			mc_socket.send(packet);
			
			// Sleep 2 seconds
			try
			{
				Thread.sleep(2000);
			}
			catch(InterruptedException e) { }
		}
		/*
		// Leave multicast group
		System.out.println("Leave multicast group " + addr + " ......");
		mc_socket.leaveGroup(group);
		mc_socket.close();
		*/
	}
}