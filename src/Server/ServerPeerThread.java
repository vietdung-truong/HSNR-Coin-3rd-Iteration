package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class ServerPeerThread extends Thread {
	private Socket socket = null;
	 
    public ServerPeerThread(Socket socket) {
        super("PeerThread");
        this.socket = socket;
    }
     
    public void run() {
 
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine, outputLine;
            ServerPeerProtocol kkp = new ServerPeerProtocol();
            outputLine = kkp.processInput("connection-request");
            out.println(outputLine);
            System.out.println("sending out connection request");
 
            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
