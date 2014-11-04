package nudat.app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

/**
 * Class for receiving messages from a udp socket
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatCommunicate {
    /**
     * The maximum number of bytes we can send in a UDP packet
     */
    protected static final int MAX_UDP_SIZE = 65507;
    
    /**
     * Function that receives a message from the datagram socket
     *
     * @param udpServerSocket
     *      is the socket to get the message from
     * @return an array of bytes containing the packet
     */
    protected static DatagramPacket receiveMessage(DatagramSocket udpServerSocket)
            throws IOException {
        // the message we receive
        byte[] receiveBuffer = new byte[MAX_UDP_SIZE];

        // the packet to place the message in
        DatagramPacket getPacket = new DatagramPacket(receiveBuffer,
                receiveBuffer.length);

        // get the next packet
        udpServerSocket.receive(getPacket);

        // return the packet
        return getPacket;
    }

    /**
     * Shorten the byte buffer in the packet with a length exectly the same
     * size as the data within
     *
     * @param packet
     *      the packet to get the buffer from and shorten
     * @return the shortened message
     */
    protected static byte[] shortenMessage(DatagramPacket packet) {
        // place the contents of the packet into a properly sized buffer and
        // return it
        byte[] received = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), 0, received, 0, received.length);
        return received;
    }
}
