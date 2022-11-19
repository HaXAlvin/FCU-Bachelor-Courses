//*******************************************************************
//*  Network Programming - Unit 5 User Datagram Protocol            *
//*  Program Name: SAWSocket                                        *
//*  This program implements a reliable connection based on stop-   *
//*  and-wait flow control. The program only provides one connection*                                        *
//*  2017.09.05                                                     *
//*******************************************************************
package SAWSocket;
//javac -d . SAWSocket.java
//javac UDPClient3.java
//javac UDPServer3.java

import javax.swing.*;
import java.net.*;
import java.io.*;

public class SAWSocket
{
	// Constant
	public static final	int SocketIdle = 1000;		// 10 second
	public static final	int SleepIdle = 100;			// 1 sec
	public static final	int BufferSize = 1024;
	public static final boolean Debug = true;
	
	DatagramSocket 	socket;
	InetAddress 	peerAddr;					
	int				peerPort;
	boolean			isServer;
	ReceiveD 		rDaemon;		// Receive daemon
	int 			PackageCount;
	
	// The following variables are for message between process and daemon
	// These variables must be accessed in the critical section
	byte []			CS_buf = new byte[BufferSize];			// save received message
	int				CS_length;								// save receive message length
	boolean			CS_busy;								// if true, CS_buf contains data
	int				CS_sn_send, CS_sn_receive, CS_ack_sn;	// sn_send, sequence number for send DATA 
															// sn_receive, sequence number for next DATA
															// ack_sn, sequence number for acknowledgement
	boolean			CS_running;								// true after SAWsocket is active (created)

	synchronized public int get_sn_receive()
	{
		return CS_sn_receive;
	}

	synchronized public int get_sn_send()
	{
		return CS_sn_send;
	}
	// TODO setsnsned
	synchronized public int add_sn_receive()
	{
		CS_sn_receive = (CS_sn_receive +1) % 10;
		// TODO
		return CS_sn_receive;
	}

	synchronized public int add_sn_send()
	{
		CS_sn_send = (CS_sn_send + 1) % 10;
		// TODO
		return CS_sn_send;
	}

	synchronized public void reveive_ack(int sn)
	{
		CS_ack_sn = sn;
		notify();
	}

	synchronized public int get_ack_sn()
	{
		return CS_ack_sn;
	}
	
	synchronized public boolean has_data()
	{
		return CS_busy;
	}

	synchronized public void copy2CS_buf(byte [] src_buf, int length) // copy received message to CS_buf
	{
		int	i;

		CS_length = length - 5;
		for(i = 0 ; i < BufferSize ; i++)
		    CS_buf[i] = src_buf[i+5];
		CS_busy = true;
	}

	synchronized public int copy4CS_buf(byte [] dest_buf)	// copy message in CS_buf to dest_buf
	{
		int	i;
		
		for(i = 0 ; i < BufferSize ; i++)
			dest_buf[i] = CS_buf[i];
		CS_busy = false;
		return CS_length;
	}

	synchronized public void wait_data() throws Exception	// wait receive data in CS_buf
	{
		wait();
	}

	synchronized public void data_ready()	// notify CS_buf has data
	{
		notify();
	}

	synchronized public void wait_ack() throws Exception	
	{
		wait(SocketIdle);
	}

	synchronized public boolean is_running()	// check if SAWSocket() is active
	{
		return CS_running;
	}

	public SAWSocket(int port,int packageCount)	throws Exception	// Constructor for server
	{
		socket = new DatagramSocket(port);
		// peerAddr & peerPort is given after the accept() is completed
		CS_busy = false;
		CS_sn_send = 0; 
		CS_sn_receive = 0; 
		CS_ack_sn = -1;
		// TODO
		CS_running = true;
		isServer = true;	// Server
		PackageCount = packageCount;
	}

	public SAWSocket(InetAddress addr, int port,int packageCount) throws Exception		// Constructor for client
	{
		// Construct a datagram socket and bind it to any available port
		socket = new DatagramSocket();
		peerAddr = addr;
		peerPort = port;
		CS_busy = false;
		CS_sn_send = 0;
		CS_sn_receive = 0;
		CS_ack_sn = -1;
		// TODO
		CS_running = true;
		isServer = false;	// Client
		PackageCount = packageCount;
	}

	public void accept() throws Exception
	{
		DatagramPacket 	packet;	
		byte []			buf = new byte[BufferSize];

		if(!isServer)	// This method can only be called by server
		{
			System.out.println("accept() can only be called by server!!");
			System.exit(1);
		}
		
		// timeout of infinity
		socket.setSoTimeout(0);	
		
		// Wait for SYN
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		peerAddr = packet.getAddress();
		peerPort = packet.getPort();
		if(Debug)
		{
			String msg = "Connect from: " + peerAddr + " / " + peerPort;
			System.out.println(msg);
		}
		
		// Send SYN, ACK
		String  reply = "SYN/ACK";
		buf = reply.getBytes();
		packet = new DatagramPacket(buf, buf.length, peerAddr, peerPort);
		socket.send(packet);
		
		// Wait for ACK
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		socket.setSoTimeout(SocketIdle);	
		// Create Receive Daemon
		rDaemon = new ReceiveD(socket, peerAddr, peerPort, this);
	}

	public void connect() throws Exception
	{
		DatagramPacket 	packet;	
		byte []			buf = new byte[BufferSize];
		
		if(isServer)	// This method can only be called by client
		{
			System.out.println("connect() can only be called by client!!");
			System.exit(1);
		}

		// timeout of infinity
		socket.setSoTimeout(0);	
		
		// Send SYN
		String	request = "SYN";
		buf = request.getBytes();
		packet = new DatagramPacket(buf, buf.length, peerAddr, peerPort);
		socket.send(packet);
		if(Debug)
		{
			String msg = "Connect to: " + peerAddr + " / " + peerPort;
			System.out.println(msg);
		}
		
		// Receive SYN, ACK
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		// Send for ACK
		request = "ACK";
		buf = request.getBytes();		
		packet = new DatagramPacket(buf, buf.length, peerAddr, peerPort);
		socket.send(packet);
		
		socket.setSoTimeout(SocketIdle);	
		// Create Receive Daemon
		rDaemon = new ReceiveD(socket, peerAddr, peerPort, this);
	}

	public int receive(byte [] buf) {
		//int	sn = get_sn_receive();
		int	length;
		// TODO

		while(!has_data())
		{
			try
			{
				wait_data();
			}
			catch(Exception ignored){ }
		}

		length = copy4CS_buf(buf);
		add_sn_receive();
		return length;
	}
	// TODO change void to bowling
	public void send(byte [] buf, int length,int sendingI) throws Exception
	{
		DatagramPacket 	packet, ackPacket;
		byte [] 		sendbuf = new byte[BufferSize+5];
		byte []			ack_buf = new byte[BufferSize];
		int				i, sn_send;
		boolean			success = false;
		
		sn_send = get_sn_send();

		// first 5 bytes are M, sn_send
		sendbuf[0] = (byte)'M';
		sendbuf[1] = (byte)( (sn_send & 0xff000000) >>> 24);
		sendbuf[2] = (byte)( (sn_send & 0x00ff0000) >>> 16);
		sendbuf[3] = (byte)( (sn_send & 0x0000ff00) >>> 8);
		sendbuf[4] = (byte)( (sn_send & 0x000000ff) );
		
		for(i = 0 ; i < buf.length ; i++)
			sendbuf[5+i] = buf[i];
		
		length = length+5;

		packet = new DatagramPacket(sendbuf, length, peerAddr, peerPort);
		// TODO
		if (sendingI%10 != 0) {
			socket.send(packet);
			//System.out.println(sendingI % 10);
		}
		while(!success && sendingI % 10 == 0)
		{	
			int	ack_sn;
			
			// Send message

			socket.send(packet);

			
			if (sn_send < 9){
				//System.out.println("waiting");
				// Wait acknowledgement
				try
				{
					wait_ack();
				}
				catch(Exception ignored){ }
			}
			// TODO

			ack_sn = get_ack_sn();
			//System.out.println("ack_sn and SN_SEND "+ack_sn+" "+sn_send);
			// TODO
			if(ack_sn < sn_send)
				success = true;
			else {
				System.out.println("Send failed !! SN = " + sn_send);
				for(int j = sendingI-10 ; j < sendingI ; j++)
				{
					byte [] reBuf;
					String msg = new String("Test!! (" + j + ")");
					reBuf = msg.getBytes();
					System.out.println("ReSend : " + msg);
					sn_send = get_sn_send();

					// first 5 bytes are M, sn_send
					sendbuf[0] = (byte)'M';
					sendbuf[1] = (byte)( (sn_send & 0xff000000) >>> 24);
					sendbuf[2] = (byte)( (sn_send & 0x00ff0000) >>> 16);
					sendbuf[3] = (byte)( (sn_send & 0x0000ff00) >>> 8);
					sendbuf[4] = (byte)( (sn_send & 0x000000ff) );

					for(i = 0 ; i < buf.length ; i++)
						sendbuf[5+i] = buf[i];

					packet = new DatagramPacket(sendbuf, msg.length()+5, peerAddr, peerPort);
					socket.send(packet);
				}
			}

			
		}
	}
	
	public void close() throws Exception
	{
		DatagramPacket 	packet;
		byte [] 		buf = new byte[BufferSize+5];
		int				sn_send;
		
		sn_send = get_sn_send();

		// first 5 bytes are F, sn_send
		buf[0] = (byte)'F';
		buf[1] = (byte)( (sn_send & 0xff000000) >>> 24);
		buf[2] = (byte)( (sn_send & 0x00ff0000) >>> 16);
		buf[3] = (byte)( (sn_send & 0x0000ff00) >>> 8);
		buf[4] = (byte)( (sn_send & 0x000000ff) );
	
		packet = new DatagramPacket(buf, buf.length, peerAddr, peerPort);
		socket.send(packet);
		socket.close();
		CS_running = false;
		rDaemon.join();		// Waiting Receive daemon
	}
}

class ReceiveD extends Thread	// Receive daemon
{
	DatagramSocket 					socket;
	InetAddress 					peerAddr;					
	int								peerPort;
	boolean							running;
	SAWSocket						DATA;
	
	ReceiveD(DatagramSocket s, InetAddress sAddr, int sPort, SAWSocket data)		// Constructor
	{
		socket = s;
		peerAddr = sAddr;
		peerPort = sPort;
		DATA = data;
		running = true;
		start();
	}
	
	public void run() 	// execute after thread has been initialized
	{
		byte []			buf = new byte[DATA.BufferSize+5]; // buf[0] = (M)essage/(A)ck, buf[1-4] = SN
		DatagramPacket 	packet;
		int				type, sn;
				
		packet = new DatagramPacket(buf, buf.length);
		while(DATA.is_running())
		{
			try
			{
				socket.receive(packet);
			
				type = (int) buf[0];
				sn = (int) buf[1];
				sn = sn * 256 + (int) buf[2];
				sn = sn * 256 + (int) buf[3];
				sn = sn * 256 + (int) buf[4];

				if(type == (int)'M')
				{
					// TODO
					if(sn <= DATA.get_sn_receive())	// receive new data
					{
						while(DATA.has_data())
						{ // data still in CS_buf
							try
							{
								Thread.sleep(DATA.SleepIdle);
							} catch (Exception ignored){ }
						}
						
						DATA.copy2CS_buf(buf, packet.getLength());
						DATA.data_ready();
						sn = (sn + 1) % 10;	// for acknowledgement
						// TODO
					}
					else if(DATA.Debug)
					{
						System.out.println("Duplicate message. SN = " + sn);
						// This program does not consider the case that the system lost the ACK message.
					}
					// if (sn != DATA.get_sn_receive()), this is a reply message
					//    that is, sender losts the previous acknowledgement message
					
					// Reply ACK (A, sn_receive)
					// 
					byte []		ack_buf = new byte[DATA.BufferSize+5];
					ack_buf[0] = (byte)'A';
					ack_buf[1] = (byte)( (sn & 0xff000000) >>> 24);
					ack_buf[2] = (byte)( (sn & 0x00ff0000) >>> 16);
					ack_buf[3] = (byte)( (sn & 0x0000ff00) >>> 8);
					ack_buf[4] = (byte)( (sn & 0x000000ff) );
					DatagramPacket ack_packet = new DatagramPacket(ack_buf, ack_buf.length, peerAddr, peerPort);

					socket.send(ack_packet);
					//System.out.println("did i send?");
				}
				else if (type == (int)'A')
				{

					if(sn <= DATA.get_sn_send())	// the first time receive ACK after Send message
					{
						DATA.reveive_ack(sn);
						//System.out.println("add sn_send");
						DATA.add_sn_send();
					}
					else if(DATA.Debug)
					{
						//System.out.println("Duplicate ACK. SN = " + sn);
					}
					//System.out.println("SN:"+sn +" sn_send:"+DATA.get_sn_send());
				}
				else if (type == (int)'F')
				{
					// Reply ACK (A, sn_receive)
					byte []		ack_buf = new byte[DATA.BufferSize+5];
					ack_buf[0] = (byte)'A';
					ack_buf[1] = (byte)( (sn & 0xff000000) >>> 24);
					ack_buf[2] = (byte)( (sn & 0x00ff0000) >>> 16);
					ack_buf[3] = (byte)( (sn & 0x0000ff00) >>> 8);
					ack_buf[4] = (byte)( (sn & 0x000000ff) );
					DatagramPacket ack_packet = new DatagramPacket(ack_buf, ack_buf.length, peerAddr, peerPort);
					socket.send(ack_packet);								
				}
				else if(DATA.Debug)
				{
					System.out.println("Message error. SN = " + sn);
				}
			}
			catch(Exception e)
			{
				if(DATA.Debug)
					System.out.println("Timeout!! Waiting message SN = " + DATA.get_sn_receive());
			}
		}
	}
}