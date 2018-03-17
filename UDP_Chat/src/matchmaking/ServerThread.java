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
			String username = null;
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

				if (msg.split(";")[0].equals("connect to player")) {
					username = msg.split(";")[1];
					String oponent = msg.split(";")[2];
					Player player = new Player(username, oponent, out);
					server.checkPlayers("matchmaking", player);
				}
			}
			sock.close();
			System.out.println("[" + sock.getInetAddress() + " : " + sock.getPort() + "] Disconnected");

			// kada se diskonektuje da se proveri i izbaci iz playerPool
			if (username != null) {
				Player player = new Player(username, "random", out);
				server.checkPlayers("remove", player);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
