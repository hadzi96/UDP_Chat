package matchmaking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BindingServer implements Runnable {

	private int clientPort1, clientPort2;
	private Player p1, p2;

	public BindingServer(int clientPort1, int clientPort2, Player p1, Player p2) {
		this.clientPort1 = clientPort1;
		this.clientPort2 = clientPort2;
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public void run() {
		try {

			DatagramSocket serverSocket1 = new DatagramSocket(clientPort1);

			System.out.println("Waiting for Client 1 on Port " + serverSocket1.getLocalPort());

			DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
			p1.out.println(clientPort1 + "");
			serverSocket1.receive(receivePacket);

			InetAddress IPAddress1 = receivePacket.getAddress();
			int port1 = receivePacket.getPort();
			String msgInfoOfClient1 = IPAddress1 + "-" + port1 + "-";

			System.out.println("Client1: " + msgInfoOfClient1);

			DatagramSocket serverSocket2 = new DatagramSocket(clientPort2);

			System.out.println("Waiting for Client 2 on Port " + serverSocket2.getLocalPort());

			receivePacket = new DatagramPacket(new byte[1024], 1024);
			p2.out.println(clientPort2 + "");
			serverSocket2.receive(receivePacket);

			InetAddress IPAddress2 = receivePacket.getAddress();
			int port2 = receivePacket.getPort();
			String msgInfoOfClient2 = IPAddress2 + "-" + port2 + "-";

			System.out.println("Client2:" + msgInfoOfClient2);

			serverSocket1.send(new DatagramPacket(msgInfoOfClient2.getBytes(), msgInfoOfClient2.getBytes().length,
					IPAddress1, port1));

			serverSocket2.send(new DatagramPacket(msgInfoOfClient1.getBytes(), msgInfoOfClient1.getBytes().length,
					IPAddress2, port2));

			serverSocket1.close();
			serverSocket2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}