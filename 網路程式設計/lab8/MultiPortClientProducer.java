//*******************************************************************
//*  Network Programming - Unit 8 Non-blocking Socket               *
//*  Program Name: MultiPortClient                                  *
//*  The program creates a nonblocking mode socket.                 *
//*  The program connects to multi-port server and send/receive     *
//*     message.                                                    *
//*  The program gets the server IP from args[0], port from args[1].*
//*  The first message to send is arg[2] and the second message     *
//*     to send is args[3].                                         *
//*  2016.02.04                                                     *
//*******************************************************************
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.net.*;
import java.io.*;

class MultiPortClientProducer
{
    public static void main(String args[])
    {
        if(args.length < 3)
        {
            System.out.println("Usage: java MultiPortClient server_ip port message");
            System.exit(1);
        }

        try
        {
            // Creates a socket channel
            SocketChannel sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress(args[0], Integer.parseInt(args[1])));

            // if the socket has connected, sc.finishConnect() will return false
            for (int loopcount = 0 ; !sc.finishConnect() ; loopcount++)
            {
                // do something
                System.out.println("Loop count = " + loopcount);
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    System.err.println(e);
                }
            }
            // Connection established

            String recS;
            // Send message to server
            ByteBuffer buffer = ByteBuffer.wrap(args[2].getBytes());
            sc.write(buffer);
            // Receive message
            ByteBuffer b = ByteBuffer.allocate(1000);
            int len = sc.read(b);    // read message from sc (non-blocking)

            while (len == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                len = sc.read(b);
            }
            recS = new String(b.array(), 0, len);
            System.out.println("Receive " + recS);
            sc.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}