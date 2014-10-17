package nudat.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

        try(Socket serverSocket = new Socket(serverIP, serverPort)){
            OutputStream out = serverSocket.getOutputStream();
            InputStream in = serverSocket.getInputStream();

            out.write("Hello World".getBytes());


            in.read(byte[] arg0);
        } catch (IOException e) {
            System.err.println("Could not communicate to the server");
            System.exit(1);
        }
    }
}
