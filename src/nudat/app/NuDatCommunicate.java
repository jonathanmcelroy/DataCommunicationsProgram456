package nudat.app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class NuDatCommunicate {

    /**
     * The maximum number of bytes we can send in a UDP packet
     */
    private static final int MAX_UDP_SIZE = 65507;
    
    protected static byte[] recieveMessage(DatagramSocket udpServerSocket)
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
