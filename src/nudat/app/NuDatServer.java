package nudat.app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;
import nudat.protocol.NuDatMessage;
import nudat.protocol.NuDatResponse;

public class NuDatServer {

    /**
     * The maximum number of bytes we can send in a UDP packet
     */
    private static final int MAX_UDP_SIZE = 65507;

    /**
     * Main function for running a server.
     *
     * @param args
     *      Args from the command line. Must be of the form: java NuDatServer
     *      serverPort
     */
    public static void main(String[] args) {
        // Check for correct usage
        if(args.length != 1) {
            System.err.println(
                "USAGE: java NuDatServer serverPort");
            System.exit(1);
        }

        // Get the port the server is to run on
        int serverPort = 0;
        try {
            serverPort = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e) {
            System.out.println("Port must be a valid integer");
        }

        // Create the server
        try(DatagramSocket udpSocket = new DatagramSocket(serverPort)) {
            while(true) {
                try {
                NuDatMessage message = NuDatMessage.decode(receiveQuery(udpSocket));
                }
                catch(NuDatException e){
                    // TODO: log error
                }

                NuDatResponse response = null;
                sendResponse(response, udpSocket);
            }
        }
        catch(SocketException e) {
            System.err.println("Problem creating the server");
            System.exit(1);
        }
        catch(IOException e) {
            System.err.println("Problem communicating with client");
            System.exit(1);
        }
    }

    /**
     * Function that returns a single query from the socket
     *
     * @param udpSocket
     *      the udp socket to get the message from
     * @return the message recieved
     */
    public static byte[] receiveQuery(DatagramSocket udpSocket) throws IOException {
        // the message we receive
        byte[] receiveBuffer = new byte[MAX_UDP_SIZE];

        // the packet to place the message in
        DatagramPacket getPacket = new DatagramPacket(receiveBuffer,
                receiveBuffer.length);

        // get the next packet
        udpSocket.receive(getPacket);

        // place the contents of the packet into a properly sized buffer and
        // return it
        byte[] received = new byte[getPacket.getLength()];
        System.arraycopy(receiveBuffer, 0, received, 0, received.length);
        return received;
    }

    /**
     * Function that sends a reposonse through the socket
     *
     * @param response
     *      the reponse to send
     * @param udpSocket
     *      the socket to send through
     */
    public static void sendResponse(NuDatResponse response, DatagramSocket udpSocket) {

    }


}
