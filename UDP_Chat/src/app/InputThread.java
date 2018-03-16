package app;

import java.util.Scanner;

public class InputThread implements Runnable {

	private UDPHolePunchingClient client;

	public InputThread(UDPHolePunchingClient client) {
		super();
		this.client = client;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Scanner in = new Scanner(System.in);
				String str = in.nextLine();

				client.ChangeStr("*$" + str + "$");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
