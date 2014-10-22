package nudat.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;

public class NuDatClient {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.err.println("USAGE: java NuDatClient serverIP serverPort responseNumber");
            System.exit(1);
        }

        String serverIP = args[0];
        int serverPort = Integer.parseInt(args[1]);
        int responseNumber = Integer.parseInt(args[2]);

        NuDatQuery query = new NuDatQuery(0, responseNumber);

        try(DatagramSocket udpServerSocket = new DatagramSocket()) {
            InetAddress address = null;
            try {
                address = InetAddress.getByName(serverIP);
                //udpServerSocket.connect(address, serverPort);
            }
            catch(UnknownHostException e) {
                System.err.println("Unknown host " + serverIP);
                System.exit(1);
            }
            if(address == null) {
                System.err.println("Cannot connect to host " + serverIP);
                System.exit(1);
            }

            System.out.println(1);

            byte[] message = query.encode();
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, serverPort);
            udpServerSocket.send(sendPacket);

            byte[] receive = new byte[5];
            DatagramPacket getPacket = new DatagramPacket(receive, receive.length);
            System.out.println(2);
            udpServerSocket.receive(getPacket);

            System.out.println(3);

            System.out.println(new String(getPacket.getData()));
            //in.read(byte[] arg0);
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

    private static void sendQuery(NuDatQuery query, MessageOutput out)
    throws IOException, NuDatException {
        out.write(query.encode());

    }

    private static String receiveResponse(BufferedReader in) throws IOException {
        return in.readLine();
    }
}
