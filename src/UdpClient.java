import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UdpClient implements AutoCloseable {

	public DatagramSocket socket;
	private InetAddress serverAddr;
	private int serverPort;

	UdpClient(String ip, int port) throws SocketException, UnknownHostException {
		serverAddr = InetAddress.getByName(ip);
		serverPort = port;
		socket = new DatagramSocket();
	}
	
	void start() {
		String input = "";
		while (!input.equals("quit")) {
			System.out.print(" - talk to server: ");
			input = System.console().readLine();
			
			try {
				{
					// send message
					byte[] buf = input.getBytes();
			        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, serverPort);
			        socket.send(packet);
				}
		        
				{
			        // receive response
				    byte[] buf = new byte[256];
			        DatagramPacket packet = new DatagramPacket(buf, buf.length);
			        socket.receive(packet);

			        // display response
			        String received = new String(packet.getData(), 0, packet.getLength());
			        System.out.println(" - server respose: " + received);
				}
			} catch (IOException e) {
				e.printStackTrace();
				input = "quit";
			}
		}
	}
	
	@Override
	public void close() throws Exception {
		if (socket != null) socket.close();
	}
}
