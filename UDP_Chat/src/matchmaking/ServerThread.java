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
	private String[] request;
	
	String username = null;
	String password = null;
	String oponent = null;
	Player player = null;

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

				request = msg.split(";");
				if (request[0].equals("connect to player")) {
					username = request[1];
					oponent = request[2];
					player = new Player(username, oponent, out);
					server.matchmaking("connect", player);
				}
				else {
					if(request[0].equals("login"))
					{
						username = request[1];
						password = request[2];
						
					}
				}
			}
			sock.close();
			System.out.println("[" + sock.getInetAddress() + " : " + sock.getPort() + "] Disconnected");

			// kada se diskonektuje da se proveri i izbaci iz playerPool
			if (player != null) {
				server.matchmaking("remove", player);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
