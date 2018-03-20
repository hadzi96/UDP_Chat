package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHolePuncher {

	private byte[] sendData;
	private DatagramPacket sendPacket;
	private InetAddress ip;
	private int port;
	private DatagramSocket clientSocket;
	private DatagramPacket receivePacket;
	private String response;
	private String[] splitResponse;
	private int localPort;

	private String message = "check";
	private boolean connected = false;
	private int counter = 0;

	public UDPHolePuncher(String serverIp, int serverPort, String oponent) throws Exception {
		clientSocket = new DatagramSocket();
		sendData = (oponent+";").getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverIp), serverPort);
		clientSocket.send(sendPacket);

		receivePacket = new DatagramPacket(new byte[1024], 1024);
		clientSocket.receive(receivePacket);

		response = new String(receivePacket.getData());
		splitResponse = response.split("-");
		ip = InetAddress.getByName(splitResponse[0].substring(1));
		port = Integer.parseInt(splitResponse[1]);

		System.out.println("IP: " + ip + " PORT: " + port);

		localPort = clientSocket.getLocalPort();
		clientSocket.close();
		clientSocket = new DatagramSocket(localPort);
		clientSocket.setSoTimeout(1000);

		// InputThread is used to get input message from user
		InputThread inpT = new InputThread(this);
		new Thread(inpT).start();

		while (true) {

			try {

				String str = ChangeStr("check");
				sendData = str.getBytes();

				sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
				clientSocket.send(sendPacket);

				receivePacket.setData(new byte[1024]);
				clientSocket.receive(receivePacket);
				String msg = new String(receivePacket.getData());

				// accepted format *$message$ = message
				if (msg.charAt(0) == '*') {
					msg = msg.substring(msg.indexOf("$") + 1, msg.lastIndexOf("$"));
					System.out.println("Message: (" + ip + ") : " + msg);
				} else {
					if (connected == false && counter >= 20) {
						System.out.println("Connected to peer: " + ip);
						connected = true;
					}
					if (counter < 20)
						counter++;
				}

			} catch (Exception e) {
				System.out.println("Reconnecting");
				connected = false;
				counter = 0;
			}

		}
	}

	public synchronized String ChangeStr(String str) {
		String tmp = message;
		message = str;
		return tmp;
	}

	public static void main(String[] args) {
		try {
			new UDPHolePuncher("hadziserver.ddns.net", 7070, "Hello");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
