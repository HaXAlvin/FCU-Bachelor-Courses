//*******************************************************************
//*  Network Programming - Unit 5 User Datagram Protocol            *
//*  Program Name: NTP_Client.java                                       *
//*  This program sends one UDP message to server and wait server   *
//*  reply.                                                         *
//*  2020.08.19                                                     *
//*******************************************************************
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class NTPMessage
{
	// NTP Packet Format       Total: 384 bits or 48 bytes.
    //uint8_t li_vn_mode;      // Eight bits. li, vn, and mode.
                               // li.   Two bits.   Leap indicator.
                               // vn.   Three bits. Version number of the protocol.
                               // mode. Three bits. Client will pick mode 3.

    //uint8_t stratum;         // Eight bits. Stratum level of the local clock.
    //uint8_t poll;            // Eight bits. Maximum interval between successive messages.
    //uint8_t precision;       // Eight bits. Precision of the local clock.

    //uint32_t rootDelay;      // 32 bits. Total round trip delay time.
    //uint32_t rootDispersion; // 32 bits. Max error aloud from primary clock source.
    //uint32_t refId;          // 32 bits. Reference clock identifier.

    //uint32_t refTm_s;        // 32 bits. Reference time-stamp seconds.
    //uint32_t refTm_f;        // 32 bits. Reference time-stamp fraction of a second.

    //uint32_t origTm_s;       // 32 bits. Originate time-stamp seconds.
    //uint32_t origTm_f;       // 32 bits. Originate time-stamp fraction of a second.

    //uint32_t rxTm_s;         // 32 bits. Received time-stamp seconds.
    //uint32_t rxTm_f;         // 32 bits. Received time-stamp fraction of a second.

    //uint32_t txTm_s;         // 32 bits and the most important field the client cares about. Transmit time-stamp seconds.
    //uint32_t txTm_f;         // 32 bits. Transmit time-stamp fraction of a second.
	
	public byte		leapIndicator = 0;
	public byte 	version = 3;
	public byte 	mode = 0;
	
	public short 	stratum = 0;
	public byte 	pollInterval = 0;
	public byte 	precision = 0;
	public double 	rootDelay = 0;
	public double 	rootDispersion = 0;
	public byte[] 	referenceIdentifier = { 0, 0, 0, 0 };
	public double 	referenceTimestamp = 0;
	public double 	originateTimestamp = 0;
	public double 	receiveTimestamp = 0;
	public double 	transmitTimestamp = 0;
	
	// Constructor
	public NTPMessage(byte [] array)
	{
		leapIndicator = (byte)((array[0] >> 6) & 0x3);	// two bits
		version = (byte)((array[0] >> 3) & 0x7);		// three bits
		mode = (byte)(array[0] & 0x7);					// three bits
		
		stratum = byte2short(array[1]);
		pollInterval = array[2];
		precision = array[3];
		
		rootDelay = (array[4] * 256.0) + byte2short(array[5])	// fraction point between bits 15 and 16
					+ (byte2short(array[6]) / 256.0) + (byte2short(array[7]) / 65536.0);
		rootDispersion = (byte2short(array[8]) * 256.0) + byte2short(array[9])  // fraction point between bits 15 and 16
					+ (byte2short(array[10]) / 256.0) + (byte2short(array[11]) / 65536.0);
		
		referenceIdentifier[0] = array[12];
		referenceIdentifier[1] = array[13];
		referenceIdentifier[2] = array[14];
		referenceIdentifier[3] = array[15];
		
		referenceTimestamp = getTimestamp(array, 16);
		originateTimestamp = getTimestamp(array, 24);
		receiveTimestamp = getTimestamp(array, 32);
		transmitTimestamp = getTimestamp(array, 40);
	}
	
	public String toString()
	{
		String precisionStr = new DecimalFormat("0.#E0").format(Math.pow(2,	precision));
		return "Leap indicator: " + leapIndicator + "\n"
			 + "Version: " + version + "\n"
			 + "Mode: "	+ mode + "\n"
			 + "Stratum: " + stratum + "\n"
			 + "Poll: " + pollInterval + "\n"
			 + "Precision: " + precision + "\n"
			 + "Root delay: " + new DecimalFormat("0.00").format(rootDelay * 1000) + " ms\n"
			 + "Root dispersion: " + new DecimalFormat("0.00").format(rootDispersion * 1000) + " ms\n"
			 + "Reference identifier: " + refId2String(referenceIdentifier, stratum, version) + "\n" 
			 + "Reference timestamp: "	+ timestamp2String(referenceTimestamp) + "\n"
			 + "Originate timestamp: "	+ timestamp2String(originateTimestamp) + "\n"
			 + "Receive timestamp: " + timestamp2String(receiveTimestamp) + "\n"
			 + "Transmit timestamp: " + timestamp2String(transmitTimestamp);
	}
	
	
	
	// unsigned byte to short
	public static short byte2short(byte b)
	{
		if((b & 0x80) == 0x80)	// negitave bit on
			return((short)(0x80 + (b & 0x7F)));
		else
			return((short) b);
	}
	
	public static double getTimestamp(byte[] array, int base)
	{
		double	r = 0.0;
		int		i;
		
		for(i = 0 ; i < 8 ; i++)
			r += (byte2short(array[base+i]) * Math.pow(2, (3-i)*8));
		return(r);
	}	
	
	public static String timestamp2String(double timestamp)
	{
		if(timestamp == 0)
			return("0");
		
		// timestamp is relative to 1900, utc is used by Java and is relative to 1970
		double utc = timestamp - (2208988800.0);

		// milliseconds
		long ms = (long) (utc * 1000.0);

		// date/time
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ms));

		// fraction
		double fraction = timestamp - ((long) timestamp);
		String fractionSting = new DecimalFormat(".000000").format(fraction);

		return date + fractionSting;
	}
	
	public static String refId2String(byte[] ref, short stratum, byte version)
	{
		// In the case of NTP Version 3 or Version 4 stratum-0 (unspecified)
		// or stratum-1 (primary) servers, this is a four-character ASCII
		// string, left justified and zero padded to 32 bits.
		if (stratum == 0 || stratum == 1)
			return new String(ref);

		// In NTP Version 3 secondary servers, this is the 32-bit IPv4
		// address of the reference source.
		else if (version == 3) {
			return byte2short(ref[0]) + "."	+ byte2short(ref[1]) + "."
					+ byte2short(ref[2]) + "." + byte2short(ref[3]);
		}

		// In NTP Version 4 secondary servers, this is the low order 32 bits
		// of the latest transmit timestamp of the reference source.
		else if (version == 4) {
			return "" + ((byte2short(ref[0]) / 256.0) + (byte2short(ref[1]) / 65536.0)
					+ (byte2short(ref[2]) / 16777216.0) + (byte2short(ref[3]) / 4294967296.0));
		}
		return "";
	}	
}

public class NTP_Client
{
	public static byte[] getRequest()
	{
		byte [] buf = new byte[48]; 
		int		i;
		Double 	txTime = (System.currentTimeMillis() / 1000.0) + 2208988800.0;
		
//		System.out.println("Current time:" + NTPMessage.timestamp2String(txTime));
		buf[0] = (byte) 0x1b; // Represents 00,011,011 in base 2.
		
		// buf[1-39] all zero
		for(i = 1 ; i < 40 ; i++)
			buf[i] = (byte) 0;
		
		// encode txTime to buf[40-47]
		// Converts a double into a 64-bit fixed point
		for (i = 0; i < 8; i++) {
			// 2^24, 2^16, 2^8, .. 2^-32
			double base = Math.pow(2, (3 - i) * 8);
			buf[40 + i] = (byte) (txTime / base);
			// Subtract captured value from remaining total
			txTime = txTime	- (double) (NTPMessage.byte2short(buf[40 + i]) * base);
		}
		
		return buf;
	}
	
	public static void main(String args[]) throws Exception
	{
		int		port = 123;
		byte []	sbuf = getRequest();
		byte []	rbuf = new byte[1024];
		
		if(args.length < 1)
		{
			System.out.println("Usage: java NTP_Client ip_address");
		}
		else
		{
			InetAddress addr = InetAddress.getByName(args[0]);

			// Construct a datagram socket and bind it to any available port
			DatagramSocket socket = new DatagramSocket();
			
			// Construct a datagram packet for sending packets of length length to 
			// the specified port number on the specified host.
			DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, addr, port);
			// Send message
			socket.send(packet);
			
			// Construct a DatagramPacket for receiving packet
			DatagramPacket recPacket = new DatagramPacket(rbuf, rbuf.length);
			socket.setSoTimeout(100);	
			try
			{
				// Receive a datagram packet from this socket
				socket.receive(recPacket);
				// Process message
				InetAddress senderAddr = recPacket.getAddress();
				int	senderPort = recPacket.getPort();
				int recLength = recPacket.getLength();
				String reply = new String("Receive from address : "	+ senderAddr + ", port : " + senderPort 
							+ ", Length: " + recLength);
				System.out.println(reply);
				
				NTPMessage msg = new NTPMessage(recPacket.getData());
				System.out.println(msg.toString());
			}
			catch(Exception e)
			{
				System.out.println("Receive time out!!");
				e.printStackTrace();
			}
				
			socket.close();
		}
	}
}
