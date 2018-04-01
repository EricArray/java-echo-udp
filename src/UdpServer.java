import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer implements AutoCloseable {

	private DatagramSocket listenSocket = null;

	private Thread listener = null;

	public UdpServer(int port) throws IOException {
		listenSocket = new DatagramSocket(port);
	}

	public void start() {
		startListener();

		boolean quitting = false;
		while (!quitting) {
			String input = System.console().readLine();

			if (input.equals("quit")) {
				quitting = true;
			} else {
				System.out.println("Command '" + input + "' not found");
			}
		}

		System.out.println("Closing server");
	}

	private void startListener() {
		listener = new Thread() {
			@Override
			public void run() {
				try {
					boolean closing = false;
					while (!closing) {
						byte[] buf = new byte[256];
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
					
						// receive client message
						listenSocket.receive(packet);
						
						String message = new String(packet.getData(), 0, packet.getLength());
						System.out.println(
								" - received from " + packet.getAddress() + ":" + packet.getPort() +
								" on local port " + listenSocket.getLocalPort() + " message '" + message + "'");
						
						// answer echo
						listenSocket.send(packet);
					}
				} catch (IOException e) {
					
				}
			}
			
			@Override
			public void interrupt() {
				super.interrupt();
				listenSocket.close();
			}
		};
		
		listener.start();
	}

	@Override
	public void close() throws Exception {
		// close thread
		if (listener != null) listener.interrupt();
	}
}
