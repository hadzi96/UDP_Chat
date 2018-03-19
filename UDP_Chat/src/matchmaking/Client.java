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

	private String username;
	private String password;
	private String oponent;

	private int UDP_port;

	private Scanner scan;
	private String command;

	public Client(String host, int port) throws Exception {
		sock = new Socket(host, port);
		System.out.println("Connected to server");

		out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

		while (true) {
			System.out.println("register/log in:");
			scan = new Scanner(System.in);
			command = scan.nextLine();

			if (command.equals("register")) {
				System.out.println("Enter Username: ");
				username = scan.nextLine();
				System.out.println("Enter Password: ");
				password = scan.nextLine();

				msg = "register;" + username + ";" + password;
				out.println(msg);

				msg = in.readLine();
				System.out.println(msg.split(";")[0]);
			}

			if (command.equals("log in")) {
				System.out.println("Enter Username: ");
				username = scan.nextLine();
				System.out.println("Enter Password: ");
				password = scan.nextLine();

				msg = "log in;" + username + ";" + password;
				out.println(msg);

				msg = in.readLine();
				String res = msg.split(";")[0];
				System.out.println(res);
				if (res.equals("log in successfull"))
					break;
			}
		}

		System.out.println("Enter username of oponent or \"random\" to connect to random oponent");
		oponent = scan.nextLine();

		msg = "connect to player;" + username + ";" + oponent;
		out.println(msg);

		while (true) {
			msg = in.readLine();
			if (msg.contains("wait for player"))
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
