import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    private static DatagramSocket receivingSocket;
    private static DatagramSocket sendingSocket;
    private static InetAddress serverIP;
    private static int serverPort = 6950;
    private static String username;

    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean isAccepted = false;
        receivingSocket = new DatagramSocket(6951);
        sendingSocket = new DatagramSocket();
        serverIP = InetAddress.getByName("localhost");
        Thread receiverThread = new Thread(new ClientReceiver(receivingSocket));
        Thread senderThread = new Thread(new ClientSender(sendingSocket, serverIP, serverPort));
        Thread heartbeat = new Thread(new Heartbeat(sendingSocket, serverIP, serverPort));

        System.out.println("Welcome to superchat!");
        while (!isAccepted) {
            System.out.print("Please type your name: ");
            username = input.readLine();
            isAccepted = authenticate();
        }

        heartbeat.start();
        receiverThread.start();
        senderThread.start();
    }

    public static boolean authenticate() throws Exception {
        DatagramPacket sendingPacket;
        DatagramPacket receivingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message = "JOIN " + username + ",";

        sendData = message.getBytes();
        sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        sendingSocket.send(sendingPacket);
        receivingPacket = new DatagramPacket(receiveData, receiveData.length);
        receivingSocket.receive(receivingPacket);
        message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

        if (message.startsWith("J_ER ")) {
            System.out.println(message.substring(9));
            return false;
        }

        System.out.println("You joined the server as \"" + username + "\"");
        System.out.println("Online users: " + message.substring(4));
        return true;
    }
}
