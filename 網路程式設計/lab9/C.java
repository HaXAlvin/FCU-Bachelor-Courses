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
//this is C
class C
{
	public static void main(String[] args) throws java.io.IOException
	{
		int				port = 8766;
		String			addr = "224.0.0.2";
		InetAddress		group = InetAddress.getByName(addr);

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
			
			//Receive from BS
			try{
				mc_socket.receive(packet);
				String	inMsg = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Get  BR: " + inMsg);
			}catch(SocketTimeoutException e) {
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