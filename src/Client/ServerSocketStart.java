package Client;

import java.io.IOException;
import java.net.*;
import java.net.ServerSocket;

public class ServerSocketStart extends Thread{
	
	private static int localPort;
	private static String localIP;
	
	
	
	public static int getLocalPort() {
		return localPort;
	}



	public static void setLocalPort(int localPort) {
		ServerSocketStart.localPort = localPort;
	}



	public static String getLocalIP() {
		return localIP;
	}



	public static void setLocalIP(String localIP) {
		ServerSocketStart.localIP = localIP;
	}



	public void run() {

		try (ServerSocket serverSocket = new ServerSocket(0);) {
			while (true) {
				localPort= serverSocket.getLocalPort();
				localIP = Inet4Address.getLocalHost().getHostAddress();
				System.out.println("serverSocket online");
				System.out.println("IP: " + localIP);
				System.out.println("port: " + localPort);
				
				new PeerThread(serverSocket.accept()).start();
			}

		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

}
