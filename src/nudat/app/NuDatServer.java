package nudat.app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.ResponseList;

import nudat.app.NuDatCommunicate;
import nudat.protocol.ErrorCode;
import nudat.protocol.NuDatException;
import nudat.protocol.NuDatQuery;
import nudat.protocol.NuDatMessage;
import nudat.protocol.NuDatResponse;

// TODO: other logs

/**
 * Server for the NuDat protocol
 *
 * @author Jonathan McElroy
 * @version 0.2
 */
public class NuDatServer extends NuDatCommunicate {

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
            System.err.println("Port must be a valid integer");
            System.exit(1);
        }

        // Create the twitter factory for communicating with twitter
        Twitter twitter = null;
        try {
            twitter = TwitterFactory.getSingleton();
        }
        catch(IllegalStateException e) {
            System.err.println("Missing authentication credentials");
            System.exit(1);
        }

        // Start the logger
        Logger logger = Logger.getLogger("NuDat");
        try {
            logger.addHandler(new FileHandler("connections.log"));
        }
        catch (SecurityException | IOException e) {
            System.err.println("Could not open communications.log for logging");
            System.exit(1);
        }

        // Create the server
        try(DatagramSocket udpSocket = new DatagramSocket(serverPort)) {
            while(true) {
                // Receive a packet
                DatagramPacket packet = receiveMessage(udpSocket);

                // Remember the packet source
                int clientPort = packet.getPort();
                InetAddress clientAddress = packet.getAddress();
                logger.info("Connection: " + clientAddress + ":" + clientPort);

                // get the message from the packet
                NuDatMessage message = null;
                try {
                    message = NuDatMessage.decode(shortenMessage(packet));
                }
                catch(NuDatException e) {
                    // If the packet cannot decode, send a response with the error
                    sendError(e.getErrorCode(), 0, clientAddress, clientPort,
                            udpSocket);

                    // get the next query
                    continue;
                }

                // If the packet was of the wrong type, sent a response with the error
                if(!(message instanceof NuDatQuery)) {
                    sendError(ErrorCode.UNEXPECTEDPACKETTYPE, 0, clientAddress,
                            clientPort, udpSocket);

                    // get the next query
                    continue;
                }

                // Cast the message to a query
                NuDatQuery query = (NuDatQuery)message;

                // get the query's id
                long queryId = query.getQueryId();

                // get the number of posts from the query
                int numPosts = query.getRequestedPosts();

                // get the posts from twitter
                ResponseList<Status> statuses = null;
                try {
                    statuses = twitter.getUserTimeline("432fun",
                            new Paging(1, numPosts));
                }
                catch(TwitterException e) {
                    // If we cannot communicate with twitter, send a network error to the client
                    sendError(ErrorCode.NETWORKERROR, queryId, clientAddress,
                            clientPort, udpSocket);
                    
                    // get the next query
                    continue;
                }

                // Get an array of strings from the post and add up the length of the strings
                int length = 0;
                ArrayList<String> posts = new ArrayList<String>();
                for(Status status : statuses) {
                    String text = status.getText();
                    posts.add(text);
                    length += 2 + text.length();
                }

                // create the the response to the client
                NuDatResponse response = null;
                try {
                    response = new NuDatResponse(ErrorCode.NOERROR, queryId,
                            posts);
                }
                catch(IllegalArgumentException e) {
                    continue;
                }

                // create the response array such that it is less that the maximum udp size
                byte[] responseArray = null;
                try {
                    responseArray = response.encode();
                    while(responseArray.length > MAX_UDP_SIZE) {
                        posts.remove(posts.size() - 1);
                        response.setPosts(posts);
                        responseArray = response.encode();
                    }
                }
                catch(NuDatException e) {
                    continue;
                }

                // Send the response with the posts
                sendResponse(responseArray, clientAddress, clientPort, udpSocket);
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
     * Function that sends a reposonse through the socket
     *
     * @param response
     *      the reponse to send
     * @param address
     *      the client's address
     * @param port
     *      the client's port
     * @param udpSocket
     *      the socket to send the packet through
     */
    private static void sendResponse(byte[] response, InetAddress address, int port, DatagramSocket udpSocket) {
        // create the packet
        DatagramPacket sendPacket = new DatagramPacket(response, response.length, address, port);

        // send the packet and ignore any errors
        try {
            udpSocket.send(sendPacket);
        }
        catch(IOException e) {}
    }

    /**
     * Function that sends a error response to the client
     * 
     * @param e
     *      the error code
     * @param queryId
     *      the id of the query
     * @param address
     *      the client's address
     * @param port
     *      the client's port
     * @param udpSocket
     *      the socket to send the packet through
     */
    private static void sendError(ErrorCode e, long queryId, InetAddress address, int port, DatagramSocket udpSocket) {
        // Create a response with the error
        NuDatResponse response = new NuDatResponse(e, queryId, new ArrayList<String>());
        try {
            // Send the error
            sendResponse(response.encode(), address, port, udpSocket);
        }
        catch(NuDatException e1) {}
    }


}












