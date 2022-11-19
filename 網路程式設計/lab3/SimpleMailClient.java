//*******************************************************************
//*  Network Programming - Unit 3 Application based on TCP          *
//*  Program Name: SimpleMailClient                                 *
//*  The program replies the number of mails in the mailbox.        *
//*  2017.08.09
//*  D0745378
//*  Alvin Hsueh
//*******************************************************************
import java.net.*;
import java.io.*;
import java.util.*;		// for StringTokenizer
import java.util.Scanner;
import java.lang.String;
import java.lang.Boolean;
//java SimpleMailClient 140.134.135.41 iecs01 K4ZrAHDB
class SimpleMailClient
{
	private static void mySend(BufferedOutputStream out, String s) throws IOException
	{
		int		i;
		
		for(i = 0 ; i < s.length() ; i++)
		{
			out.write((byte)s.charAt(i));
		}
		out.flush();
	}
	
	private static String myRecv(BufferedInputStream in) throws IOException
	{
		String	reply = "";
		int		c;
		
		for(c = in.read() ; c >= 0 && c != '\n' ; c = in.read())
		{
		    if(c != '\r')
		        reply += (char) c;
		}
		return reply;
	}
	
	public static void main(String args[])
	{
		Socket					client = null;
		int						port = 110;
		BufferedInputStream 	in = null;
		BufferedOutputStream 	out = null;
		byte []					buf = new byte[1000];
		String					reply;
		String					request;
		int						num;
		Scanner scanner = new Scanner(System.in);

		if(args.length < 3)
		{
		    System.out.println("Usage: java SimpleMailClient server username password");
		}
		else
		{
			try
			{	
				client = new Socket(args[0], port);
				do	// execute only once
				{
					in = new BufferedInputStream(client.getInputStream());
					out = new BufferedOutputStream(client.getOutputStream());
					
					// receive server greeting message
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					if(reply.charAt(0) != '+')
					    continue;

					// Username
					mySend(out, "USER " + args[1] + "\r\n");	// don't forget "\r\n"
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					if(reply.charAt(0) != '+')
					    continue;

					// Password					
					mySend(out, "PASS " + args[2] + "\r\n");	// don't forget "\r\n"
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					if(reply.charAt(0) != '+')
					    continue;
					
					// Status [Method 2]
					mySend(out, "STAT\r\n");					// don't forget "\r\n"
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					StringTokenizer token = new StringTokenizer(reply);
					token.nextToken();	// skip +OK
					num = Integer.parseInt(token.nextToken());	
					System.out.println("Mailbox has " + num + " mails");
					
					//DELE
					System.out.print("Please Enter A Number:");
					//String str;
					String str = scanner.nextLine();
					mySend(out,"DELE "+ str + "\r\n");
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					
					// Status [Method 2]
					mySend(out, "STAT\r\n");					// don't forget "\r\n"
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					token = new StringTokenizer(reply);
					token.nextToken();	// skip +OK
					num = Integer.parseInt(token.nextToken());	
					System.out.println("Mailbox has " + num + " mails");
					
					//Email Data
					int i=1;
					Boolean exist=false;
					for(;i<=num;i++){
						mySend(out, "TOP "+ String.valueOf(i) +" 0\r\n");

						reply = myRecv(in);
						System.out.println("****MAIL " + String.valueOf(i));
						System.out.println(reply);
						//System.out.println(reply);
						if(reply.charAt(0) != '+') {
							continue;
						}
						do{
							reply = myRecv(in);
							//System.out.println(reply);

							if(reply.compareTo(".") == 0){
								break;
							}else if(reply.length() == 0){
								continue;
							}
//							if(exist==false){
//								System.out.println("****MAIL " + String.valueOf(i));
//								exist = true;
//							}
							// TO: Subject:  Date: From:
							String substr = reply.substring(0,2);
							//System.out.println("Receive message: " + substr);
							if (substr.compareToIgnoreCase("To")==0 || substr.compareToIgnoreCase("Da")==0 || substr.compareToIgnoreCase("Fr")==0 || substr.compareToIgnoreCase("Su")==0){
								System.out.println("Receive message: " + reply);
							}
							
						}while(true);

					}
					// Read mail
					System.out.print("Please Enter A Number:");
					str = scanner.nextLine();
					mySend(out, "RETR "+str+"\r\n");					// don't forget "\r\n"
					reply = myRecv(in);
					System.out.println("Receive message: " + reply);
					if(reply.charAt(0) != '+') {
						continue;
					}
					do{
						reply = myRecv(in);
						System.out.println(reply);

						if(reply.compareTo(".") == 0){
							break;
						}

					}while(true);

					// Quit 
					mySend(out, "QUIT\r\n");
				} while(false);

				client.close();
				System.out.println("Connection closed!");
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}