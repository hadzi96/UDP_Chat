package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class P2PBinder {

	private int MAX_BLOCK_TIME = 5000;
	private int clientPort1 = 7070, clientPort2 = 7071;
	private Player p1, p2;
	private String msg;

	private DatagramSocket serverSocket1;
	private DatagramSocket serverSocket2;
	private DatagramPacket receivePacket;
	private InetAddress IPAddress1;
	private int port1;
	private String msgInfoOfClient1;
	private String p2Oponent;
	private String p1Oponent;
	private InetAddress IPAddress2;
	private int port2;
	private String msgInfoOfClient2;

	public void bind(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;

		closeSockets();
		startBinding();
	}

	public void startBinding() {
		while (true) {
			try {

				connectClient1();
				connectClient2();

				if (check() == false) {
					closeSockets();
					return;
				}

				exchangeInfo();

				closeSockets();
				break;

			} catch (SocketTimeoutException timeoutException) {

				// ovde ulazi ako duze od 5s zauzima port pa ga zato gasimo
				System.out.println("socket timed out");
				closeSockets();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("P2P binding complete");
	}

	private void connectClient1() throws Exception {

		serverSocket1 = new DatagramSocket(clientPort1);
		receivePacket = new DatagramPacket(new byte[1024], 1024);
		// set max thread blocking time
		serverSocket1.setSoTimeout(MAX_BLOCK_TIME);

		System.out.println("Waiting for Client 1 on Port " + serverSocket1.getLocalPort());
		// saljemo clientu poruku da se javi na dati port
		p1.out.println(clientPort1);

		serverSocket1.receive(receivePacket);
		msg = new String(receivePacket.getData());

		// cuvamo za proveru
		p1Oponent = msg.split(";")[0];

		IPAddress1 = receivePacket.getAddress();
		port1 = receivePacket.getPort();
		msgInfoOfClient1 = IPAddress1 + "-" + port1 + "-";
		System.out.println("Client1: " + msgInfoOfClient1);
	}

	private void connectClient2() throws Exception {

		serverSocket2 = new DatagramSocket(clientPort2);
		receivePacket = new DatagramPacket(new byte[1024], 1024);
		// set max thread blocking time
		serverSocket2.setSoTimeout(MAX_BLOCK_TIME);

		System.out.println("Waiting for Client 2 on Port " + serverSocket2.getLocalPort());
		// saljemo clientu poruku da se javi na dati port
		p2.out.println(clientPort2);

		serverSocket2.receive(receivePacket);
		msg = new String(receivePacket.getData());

		// cuvamo za proveru
		p2Oponent = msg.split(";")[0];

		IPAddress2 = receivePacket.getAddress();
		port2 = receivePacket.getPort();
		msgInfoOfClient2 = IPAddress2 + "-" + port2 + "-";

		System.out.println("Client2:" + msgInfoOfClient2);
	}

	private boolean check() {

		// proveravamo dali treba ova dva playera da povezemo
		if (!(p2Oponent.equals("random") && p1Oponent.equals("random"))) {
			if (!(p1Oponent.equals(p2.username) && p2Oponent.equals(p1.username))) {
				return false;
			}
		}

		return true;
	}

	private void exchangeInfo() throws Exception {

		serverSocket1.send(
				new DatagramPacket(msgInfoOfClient2.getBytes(), msgInfoOfClient2.getBytes().length, IPAddress1, port1));

		serverSocket2.send(
				new DatagramPacket(msgInfoOfClient1.getBytes(), msgInfoOfClient1.getBytes().length, IPAddress2, port2));
	}

	private void closeSockets() {
		if (serverSocket1 != null)
			serverSocket1.close();
		if (serverSocket2 != null)
			serverSocket2.close();

		serverSocket1 = null;
		serverSocket2 = null;
	}

}
