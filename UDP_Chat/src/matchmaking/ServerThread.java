package matchmaking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

	private Socket sock;
	private Server server;

	private String msg;

	public ServerThread(Socket sock, Server server) {
		this.sock = sock;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			System.out.println("[" + sock.getInetAddress() + " : " + sock.getPort() + "] Connected");

			PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			while (true) {
				try {
					msg = in.readLine();
				} catch (Exception e) {
					break;
				}
				if (msg == null)
					break;

				if (msg.equals("connect to player")) {
					Player player = new Player("user", out);
					server.checkPlayers(player);
				}
			}
			sock.close();
			System.out.println("[" + sock.getInetAddress() + " : " + sock.getPort() + "] Disconnected");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
