//*******************************************************************
//*  Network Programming - Unit 2 Simple Client and Server          *
//*  Program Name: GetAddressByName                                 *
//*  The program lookups the IP address of the given domain name.   *
//*  The program gets the domain name from args[0].                 *
//*  2017.08.04                                                     *
//*******************************************************************
import java.net.*;

class GetAddressByName
{
	public static void main(String args[])
	{
		InetAddress	addr;
		InetAddress	addrs[];

		if(args.length == 0)
		{
		    System.out.println("Usage: java GetAddressByName doamin_name");
		}
		else
		{
			try
			{
				addr = InetAddress.getByName(args[0]);
				System.out.println("Result of getByName()");
				System.out.println(addr);
				
				addrs = InetAddress.getAllByName(args[0]);
				System.out.println("Result of getAllByName()");
				for(InetAddress a : addrs)
				{
					System.out.println(a);
				}
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
	}
}