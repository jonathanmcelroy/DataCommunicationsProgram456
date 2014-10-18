package nudat.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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

        try(Socket serverSocket = new Socket(serverIP, serverPort)) {
            MessageOutput out = new MessageOutput(serverSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            sendQuery(query, out);
            recieveResponse(in);
            //System.out.println(in.readLine());

            //in.read(byte[] arg0);
        }
        catch(IOException e) {
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
        // TODO Auto-generated method stub
        out.write(query.encode());

    }

    private static void recieveResponse(BufferedReader in) {
        // TODO Auto-generated method stub

    }
}

