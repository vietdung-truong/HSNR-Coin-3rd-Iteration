package Server;

public class ServerPeerProtocol extends Thread {

	private static final int WAITING = 0;
	private int state = WAITING;

	private String[] clues = { "connection-request", "get-peers", "check-last", "catch-up", "Who" };

	public String processInput(String inputString) {
		String outputString = null;

		if (state == WAITING) {
			if (inputString.equals(clues[0])) {
				System.out.println("got command: connection-request");
				outputString = "connection-confirmed";
			}
		}
		return outputString;
	}
}
