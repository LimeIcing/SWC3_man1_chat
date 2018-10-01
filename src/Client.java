import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws Exception {
        int serverPort = 6950;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket receivingSocket = new DatagramSocket(6951);
        DatagramSocket sendingSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName("localhost");
        Authenticator authenticator = new Authenticator(receivingSocket, sendingSocket, serverIP, serverPort);
        Thread receiverThread = new Thread(new ClientReceiver(receivingSocket));
        Thread senderThread = new Thread(new ClientSender(sendingSocket, serverIP, serverPort));
        boolean isAccepted = false;

        System.out.print("Welcome to superchat!\nPlease type your name:");
        while (!isAccepted) {
            isAccepted = authenticator.authenticate(input.readLine());
        }

        receiverThread.start();
        senderThread.start();
    }
}
