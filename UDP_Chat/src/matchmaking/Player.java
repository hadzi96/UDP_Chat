package matchmaking;

import java.io.PrintWriter;

public class Player {

	public String username;
	public PrintWriter out;

	public Player(String username, PrintWriter out) {
		this.username = username;
		this.out = out;
	}

}
