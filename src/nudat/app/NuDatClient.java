package nudat.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.util.Random;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;
import nudat.protocol.NuDatMessage;
import nudat.protocol.NuDatResponse;

/**
 * Client for the NuDat protocol
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatClient {

    /**
     * The maximum number of bytes we can send in a UDP packet
     */
    private static final int MAX_UDP_SIZE = 65507;

    /**
     * The maximum number for the query id
     */
    private static final int MAX_QUERY_ID = 1000000;

    /**
     * Random number generator for queryId
     */
    private static final Random random = new Random();

    /**
     * Main function for running a client.
     *
     * @param args
     *      Args from the command line. Must be of the form: java NuDatClient
     *      serverIP serverPost postNumber
     */
    public static void main(String[] args) {
        // Check for correct usage
        if(args.length != 3) {
            System.err.println("USAGE: java NuDatClient serverIP serverPort postNumber");
            System.exit(1);
        }

        // Get the IP or name of the server
        String serverIP = args[0];

        // Get the port the server is running on
        int serverPort = Integer.parseInt(args[1]);

        // Get the number of posts the user wants
        int postNumber = Integer.parseInt(args[2]);

        try(DatagramSocket udpServerSocket = new DatagramSocket()) {
            // Create a query from the arguments
            NuDatQuery query = new NuDatQuery(random.nextInt(MAX_QUERY_ID), postNumber);

            // Try to connect to the address. This does not set up a connection,
            // it just validates the serverIP and makes sure the socket will
            // only send packets to and accept packets from the server
            try {
                InetAddress address = InetAddress.getByName(serverIP);
                udpServerSocket.connect(address, serverPort);
            }
            catch(UnknownHostException e) {
                System.err.println("Unknown host " + serverIP);
                System.exit(1);
            }

            // set the socket timeout to 3 seconds
            udpServerSocket.setSoTimeout(3000);

            // the array to hold the returned message
            NuDatMessage message = null;

            boolean received = false;
            while(!received) {
                // send a request for posts
                sendQuery(query, udpServerSocket);
                try {
                    // get the response. If it doesn't return after 3 seconds,
                    // raise a socket timeout exception
                    message = NuDatMessage.decode(receiveResponse(udpServerSocket));
                    received  = true;
                }
                catch(SocketTimeoutException e) {}
            }

            if(!(message instanceof NuDatResponse)) {
                throw new NuDatException(ErrorCode.UNEXPECTEDPACKETTYPE);
            }

            NuDatResponse response = (NuDatResponse)message;

            for(String post : response.getPosts()) {
                System.out.println(post);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
            System.err.println("Could not communicate to the server");
            System.exit(1);
        }
        catch(NuDatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Function that sends a query through the datagram socket.
     *
     * @param query
     *      is the query to encode and send
     * @param udpServerSocket
     *      is the socket to send the packet through
     */
    private static void sendQuery(NuDatQuery query, DatagramSocket udpServerSocket)
    throws IOException, NuDatException {
        // the message to send
        byte[] queryMessage = query.encode();

        // create the packet
        DatagramPacket sendPacket = new DatagramPacket(queryMessage,
        queryMessage.length,
        udpServerSocket.getInetAddress(),
        udpServerSocket.getPort());

        // send the packet
        udpServerSocket.send(sendPacket);
    }

    /**
     * Function that receives a message from the datagram socket
     *
     * @param udpServerSocket
     *      is the socket to get the message from
     * @return an array of bytes containing the packet
     */
    private static byte[] receiveResponse(DatagramSocket udpServerSocket)
    throws IOException {
        // the message we receive
        byte[] receiveBuffer = new byte[MAX_UDP_SIZE];

        // the packet to place the message in
        DatagramPacket getPacket = new DatagramPacket(receiveBuffer,
        receiveBuffer.length);

        // get the next packet
        udpServerSocket.receive(getPacket);

        // place the contents of the packet into a properly sized buffer and
        // return it
        byte[] received = new byte[getPacket.getLength()];
        System.arraycopy(receiveBuffer, 0, received, 0, received.length);
        return received;
    }
}







