//*******************************************************************
//*  Network Programming - Unit 2 Simple Client and Server          *
//*  Program Name: SimpleServer1                                     *
//*  The program creates a socket and waits for request.            *
//*  2017.08.04
//*  Student : D0745378
//*  Class : 資訊三甲
//*******************************************************************
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SimpleServer1
{
	public static void main(String[] args)
	{
		ServerSocket	serverSocket = null;
		Socket			sc = null;
		InputStream		in = null;
		OutputStream	out = null;
		int				port = 6666;
		byte []			buf = new byte[100];

		try
		{
		    // Creates a server socket, bound to the specified port.
			serverSocket = new ServerSocket(port);
			String nowCount = "-1";
			System.out.println("Waiting for request ...");
			try
			{
			    // Listens for a connection to be made to this socket and accepts it.
				
				sc = serverSocket.accept();
				while(!nowCount.equals("0")){
					// Read message from client
					// Returns an input stream for socket sc.
					in = sc.getInputStream();
					// Reads some number of bytes from the input stream and stores them into buf.
					buf = new byte[100];
					in.read(buf);
					nowCount = new String(buf).trim();
					System.out.println("Receive message: " + nowCount);
					if (!nowCount.equals("0")){
						int counter = Integer.parseInt(nowCount)-1;
						nowCount = String.valueOf(counter);
					}



					// Send reply message to client
					// Returns an output stream for socket sc.
					out = sc.getOutputStream();
					// Writes bytes from the specified byte array to this output stream.
					out.write(nowCount.getBytes());
					// getBytes(): Encodes this String into a sequence of bytes using the 
					// platform's default charset, storing the result into a new byte array.

				}
				in.close();
				out.close(); // Closes in/out stream and releases any system resources associated with this stream.
				    
				// Closes this socket
				sc.close();
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
