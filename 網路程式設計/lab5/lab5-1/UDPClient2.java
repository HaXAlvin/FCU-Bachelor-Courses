//*******************************************************************
//*  Network Programming - Unit 6 User Datagram Protocol            *
//*  Program Name: UDPClient1                                       *
//*  This program sends 100 UDP messages to server.                 *
//*  2016.02.04                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;

public class UDPClient2
{
	public static void main(String args[]) throws Exception
	{
		int		port = 8888;
		byte []	buf = new byte[1000];
		int 	recPort = 8777;
		int 	tryCount = 0;
		
		if(args.length < 3)
		{
			System.out.println("Usage: java UDPClient2 ip_address Number recPort");
		}
		else
		{
			InetAddress addr = InetAddress.getByName(args[0]);
			String counter = args[1];
			String origin = args[1];
			recPort = Integer.parseInt(args[2]);
			DatagramPacket packet = null;
			DatagramSocket socket = null;
			while(!counter.equals("0"))
			{
				buf = new byte[1000];
				// Encodes this String into a sequence of bytes and stores to buf.
				System.out.println(tryCount+"th Try, Send :"+counter);
				buf = counter.getBytes();
				// Constructs a datagram packet for sending packets of length length to 
				// the specified port number on the specified host.
				packet = new DatagramPacket(buf, buf.length, addr, port);
				// Constructs a datagram socket and binds it to any available port on 
				// the local host machine.
				socket = new DatagramSocket(recPort);
				socket.send(packet);
				socket.close();
				
				socket = new DatagramSocket(recPort);
				buf = new byte[1000];
				// Constructs a DatagramPacket for receiving packets
				packet = new DatagramPacket(buf, buf.length);
				
				// Constructs a datagram socket and binds it to the specified port 
				
				// Receives a datagram packet from this socket
				socket.setSoTimeout(1000);
				try{
					socket.receive(packet);
					int recLength = packet.getLength();
					counter = new String(buf, 0, recLength);
					System.out.println("Receive message :" + counter);
					
				}catch(SocketTimeoutException e){
					System.out.println("Time Out!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("Reset To "+origin);
					counter = origin;
					tryCount++;
				}
				socket.close();
			}
			System.out.println("Count End!");
		}
	}
}
