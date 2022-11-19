//*******************************************************************
//*  Network Programming - Unit 2 Simple Client and Server          *
//*  Program Name: GetAddress                                       *
//*  The program gets the addresses of network interfaces.          *
//*  2017.08.04                                                     *
//*******************************************************************
import java.net.*;

class GetAddress
{
	public static void main(String args[])
	{
        InetAddress	addr;

		try
		{
			addr = InetAddress.getLocalHost();
			System.out.println(addr);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
	}
}