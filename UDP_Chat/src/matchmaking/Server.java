package matchmaking;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private Player player = null;

	public Server() throws Exception {

		int port = 6000;
		ServerSocket ss = new ServerSocket(port);
		System.out.println("Server running on port: " + port);

		while (true) {
			Socket sock = ss.accept();

			ServerThread st = new ServerThread(sock, this);
			Thread thread = new Thread(st);
			thread.start();
		}

	}

	public synchronized void checkPlayers(Player pl) {

		if (player == null) {
			player = pl;
			pl.out.println("wait for player");
		} else {
			new Thread(new BindingServer(7070, 7071, player, pl)).start(); // ovaj thread povezuje ova dva playera
			player = null;
		}

	}

	public static void main(String[] args) {
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
