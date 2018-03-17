package matchmaking;

import java.io.PrintWriter;

public class Player {

	public String username;
	public String oponent;
	public PrintWriter out;

	public Player(String username, String oponent, PrintWriter out) {
		this.username = username;
		this.oponent = oponent;
		this.out = out;
	}

}
