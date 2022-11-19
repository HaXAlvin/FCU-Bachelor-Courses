//*******************************************************************
//*  Network Programming - Unit 2 Simple Client and Server          *
//*  Program Name: SimpleServer1                                     *
//*  The program creates a socket and waits for request.            *
//*  2017.08.04
//*  Student D0745378
//*  Class 
//*******************************************************************
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.lang.*;

class ThreadBySubclass extends Thread
{
	int				threadCount;
	Socket 			sc;
	InputStream		in = null;
	OutputStream	out = null;
	String 			nowCount = "-1";
	ThreadBySubclass(int count,Socket socket)		// Constructor
	{
		threadCount = count;
		sc = socket;
		start();
	}
	public void run()	// execute after thread has been initialized
	{
		System.out.println("Thread " + String.valueOf(threadCount) + " Connected!");
		try{
			while(!nowCount.equals("0")){
				in = sc.getInputStream();
				byte [] buf = new byte[100];
				in.read(buf);
				nowCount = new String(buf).trim();
				System.out.println("Receive message from " + String.valueOf(threadCount) + ": " + nowCount);
				if(nowCount.equals("0"))
					continue;
				int counter = Integer.parseInt(nowCount)-1;
				nowCount = String.valueOf(counter);
				out = sc.getOutputStream();
				out.write(nowCount.getBytes());
			}
			in.close();
			out.close();
			sc.close();
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
		finally
		{
			System.out.println("Thread " + String.valueOf(threadCount) + " end");
		}
	}
}

public class SimpleServer1
{
	public static void main(String[] args)
	{
		ServerSocket		serverSocket = null;
		int					port = 6666;
		int 				threadCount = 0;
		ThreadBySubclass 	thread = null;
		try
		{
		    // Creates a server socket, bound to the specified port.
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting for request ...");
			try
			{
				while((thread = new ThreadBySubclass(threadCount++,serverSocket.accept()))!=null);
			}
			catch(IOException e)
			{
				System.err.println(e);
			}
			finally
			{
				serverSocket.close();
			}
		}
		catch(IOException e)
		{
			System.err.println(e);
		}
	}
	
}
