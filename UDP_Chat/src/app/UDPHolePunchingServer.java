package app;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Stack;

public class UDPHolePunchingServer {
	
	private Stack<Client> stack = new Stack<>();

	public UDPHolePunchingServer(int clientPort1, int clientPort2) throws Exception {
		
		DatagramSocket serverSocket1 = new DatagramSocket(clientPort1);

		while(true) {
		System.out.println("Waiting for Client on Port " + serverSocket1.getLocalPort());
		
		DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
		
		
		serverSocket1.receive(receivePacket);
		

		InetAddress IPAddress1 = receivePacket.getAddress();
		int port1 = receivePacket.getPort();
		String msgInfoOfClient1 = IPAddress1 + "-" + port1 + "-";  

		System.out.println("Client1: " + msgInfoOfClient1);
		Client client = new Client(msgInfoOfClient1);
		
		if(!stack.contains(client) && stack.isEmpty()) {
			stack.push(client);
		}
		else {
			if(!stack.contains(client) && !stack.isEmpty()){
				Client c = stack.pop();
				serverSocket1.send(
						new DatagramPacket(c.msgInfoOfClient.getBytes(), c.msgInfoOfClient.getBytes().length, IPAddress1, port1));
			}
		}
		}

		/*DatagramSocket serverSocket2 = new DatagramSocket(clientPort2);

		System.out.println("Waiting for Client 2 on Port " + serverSocket2.getLocalPort());

		receivePacket = new DatagramPacket(new byte[1024], 1024);
		serverSocket2.receive(receivePacket);

		InetAddress IPAddress2 = receivePacket.getAddress();
		int port2 = receivePacket.getPort();
		String msgInfoOfClient2 = IPAddress2 + "-" + port2 + "-";

		System.out.println("Client2:" + msgInfoOfClient2);

		serverSocket1.send(
				new DatagramPacket(msgInfoOfClient2.getBytes(), msgInfoOfClient2.getBytes().length, IPAddress1, port1));
		
		Thread.sleep(5000);

		serverSocket2.send(
				new DatagramPacket(msgInfoOfClient1.getBytes(), msgInfoOfClient1.getBytes().length, IPAddress2, port2));*/

		//serverSocket1.close();
		//serverSocket2.close();
		
	}

	public static void main(String args[]) {
		try {
			new UDPHolePunchingServer(7070, 7071);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Server fail");
		}

	}
}