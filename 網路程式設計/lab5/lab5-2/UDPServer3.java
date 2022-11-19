//*******************************************************************
//*  Network Programming - Unit 5 User Datagram Protocol            *
//*  Program Name: UDPServer3                                       *
//*  This program is a server based on SAWSocket.                   *
//*  2017.09.05                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;
import java.lang.*;
import SAWSocket.SAWSocket;

public class UDPServer3
{
	public static void main(String args[]) throws Exception
	{
		int		port = 8888;
		byte []	buf = new byte[1024];
		int		length;
		if(args.length < 1)
		{
			System.out.println("Usage: java UDPServer3 packageCount");
		}
		SAWSocket	server = new SAWSocket(port,Integer.parseInt(args[0]));
		server.accept();
		System.out.println("Accepted!!");

		for(int i = 0 ; i < 100 ; i++)
		{
			length = server.receive(buf);
			String msg = new String(buf, 0, length);
			System.out.println("(" + i + ") Receive:" + msg);
		}		
		server.close();
	}
}
