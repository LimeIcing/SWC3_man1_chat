import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket receivingSocket = new DatagramSocket(6951);
        DatagramSocket sendingSocket = new DatagramSocket();

        InetAddress IP = InetAddress.getByName("localhost");

        Thread receiver = new Thread(new ClientReceiver(receivingSocket));
        Thread sender = new Thread(new ClientSender(sendingSocket, IP));

        receiver.start();
        sender.start();
        System.out.println("Welcome to superchat");
    }
}
