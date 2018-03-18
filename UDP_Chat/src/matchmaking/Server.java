package matchmaking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

	private HashMap<String, Player> playerPool = new HashMap<>(); // KEY = username ; VALUE = player

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

	public synchronized void checkPlayers(String command, Player pl) {

		if (command.equals("remove")) {
			if ((playerPool.remove(pl.username)) != null)
				System.out.println(pl.username + "removed");
			return;
		}

		if (pl.oponent.equals("random")) { // matchmaking with random oponent
			if (!playerPool.containsKey("random")) {
				playerPool.put("random", pl);
				pl.out.println("wait for player");
			} else {
				Player player = playerPool.get("random");
				playerPool.remove("random");
				new Thread(new BindingServer(7070, 7071, player, pl)).start();
			}

		} else { // matchmaking with specific user
			if (!playerPool.containsKey(pl.oponent)) {
				playerPool.put(pl.username, pl);
				pl.out.println("wait for player");
			} else {
				Player player = playerPool.get(pl.oponent);
				if (!player.username.equals(pl.oponent)) // ne slazu im se zahtevi
					return;

				playerPool.remove(pl.oponent);
				new Thread(new BindingServer(7070, 7071, player, pl)).start();
			}

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
