package app;

import java.net.InetAddress;

public class Client {

	public String msgInfoOfClient;

	public Client(String msgInfoOfClient) {
		this.msgInfoOfClient = msgInfoOfClient;
	}

	@Override
	public boolean equals(Object obj) {
		Client c = (Client) obj;
		return this.msgInfoOfClient.equals(c.msgInfoOfClient);
	}

}
