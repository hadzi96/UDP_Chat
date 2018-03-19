package matchmaking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

	private HashMap<String, Player> playerPool = new HashMap<>(); // KEY = username ; VALUE = player
	private HashMap<String, String> users = new HashMap<>(); // KEY = username; VLUE = password
	private ReentrantLock lock = new ReentrantLock();
	private P2PBinder binder = new P2PBinder();

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

	public boolean logIn(String username, String password) {
		lock.lock();
		if (users.containsKey(username)) {
			return true;
		}

		lock.unlock();
		return false;
	}

	public synchronized void matchmaking(String command, Player pl) {

		if (command.equals("remove")) {
			if (pl.oponent.equals("random")) {
				playerPool.remove("random", pl);
			} else {
				playerPool.remove(pl.username, pl);
			}

			return;
		}

		if (pl.oponent.equals("random")) { // matchmaking with random oponent
			if (!playerPool.containsKey("random")) {
				playerPool.put("random", pl);
				pl.out.println("wait for player");
			} else {
				Player player = playerPool.get("random");
				playerPool.remove("random");
				binder.bind(7070, 7071, player, pl);
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
				binder.bind(7070, 7071, player, pl);
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
