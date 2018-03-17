package matchmaking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import app.UDPHolePunchingClient;

public class Client {

	private Socket sock;
	private PrintWriter out;
	private BufferedReader in;
	private String msg;

	private int UDP_port;

	public Client(String host, int port) throws Exception {
		System.out.println("Enter your username");
		Scanner sce = new Scanner(System.in);
		String username = sce.nextLine();

		System.out.println(
				"Enter oponents username to konnect to oponent or \"random\" to connect to first available user");
		Scanner sc = new Scanner(System.in);
		String oponent = sc.nextLine();

		sock = new Socket(host, port);
		System.out.println("Connected to server");

		out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		msg = "connect to player;" + username + ";" + oponent;
		out.println(msg);

		while (true) {
			msg = in.readLine();
			if (msg.equals("wait for player"))
				continue;
			else {
				UDP_port = Integer.parseInt(msg);
				break;
			}
		}

		sock.close();
		System.out.println("pokrenuo udp punching na portu: " + UDP_port);
		new UDPHolePunchingClient(host, UDP_port, oponent);
	}

	public static void main(String[] args) {
		try {
			new Client("hadziserver.ddns.net", 6000);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
