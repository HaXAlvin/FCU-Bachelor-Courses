//*******************************************************************
//*  Network Programming - Unit 5 User Datagram Protocol            *
//*  Program Name: UDPServer2                                       *
//*  This program receives 100 UDP messages 						*
//*  2020.08.15                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;
import java.lang.*;

public class UDPServer2
{
	public static void main(String args[]) throws Exception
	{
		int		port = 8888;
		byte []	buf = new byte[1000];
		DatagramPacket packet=null;
		System.out.println("Waiting on port : " + port);
		DatagramSocket socket = null;
		String msg = "";
		while(true)
		{
			socket = new DatagramSocket(port);
			buf = new byte[1000];
			// Constructs a DatagramPacket for receiving packets
			packet = new DatagramPacket(buf, buf.length);
			
			// Constructs a datagram socket and binds it to the specified port 
			
			// Receives a datagram packet from this socket
			socket.receive(packet);
			int recLength = packet.getLength();
			InetAddress senderAddr = packet.getAddress();
			int	senderPort = packet.getPort();
			String recData = new String(buf, 0, recLength);
			
			System.out.println("Receive message '" + recData + 
									"' from address : "	+ senderAddr + 
									", port : " + senderPort);
		    socket.close();
			
			buf = new byte[1000];
		    msg = minusString(recData);
			System.out.println("Send :"+msg);
			// Encodes this String into a sequence of bytes and stores to buf.
			buf = msg.getBytes();
			// Constructs a datagram packet for sending packets of length length to 
			// the specified port number on the specified host.
			packet = new DatagramPacket(buf, buf.length, senderAddr, senderPort);
			// Constructs a datagram socket and binds it to any available port on 
			// the local host machine.
			socket = new DatagramSocket(port);
			socket.send(packet);
			socket.close();
		}
		
	}
	public static String minusString(String data){
		return String.valueOf(Integer.parseInt(data)-1);
	}
}
