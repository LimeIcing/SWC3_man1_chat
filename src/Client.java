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

    //TODO: When done, write a method to colour sout's (https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println#5762502)
    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        boolean isAccepted = false;
        receivingSocket = new DatagramSocket();
        sendingSocket = new DatagramSocket();
        serverIP = InetAddress.getByName("localhost");

        System.out.println("Welcome to the chat!");
        while (!isAccepted) {
            System.out.print("Please type your name: ");
            username = input.readLine();

            //+[0-9]+[-]+[_]
            if (username.matches("[a-zA-Z0-9_-]+") && username.length() < 13) {
                isAccepted = authenticate();
            } else {
                System.out.println("Username can only contain letters, numbers, - and _");
            }
        }

        Thread receiverThread = new Thread(new ClientReceiver(receivingSocket));
        Thread senderThread = new Thread(new ClientSender(sendingSocket, serverIP, serverPort, username));
        Thread heartbeat = new Thread(new Heartbeat(sendingSocket, serverIP, serverPort));
        heartbeat.start();
        receiverThread.start();
        senderThread.start();
    }

    private static boolean authenticate() throws Exception {
        DatagramPacket sendingPacket;
        DatagramPacket receivingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message = "JOIN " + username + ", " + serverIP + ':' + serverPort;

        sendData = message.getBytes();
        sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        sendingSocket.send(sendingPacket);
        receivingPacket = new DatagramPacket(receiveData, receiveData.length);
        receivingSocket.receive(receivingPacket);
        message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

        if (message.startsWith("J_ER ")) {
            System.out.println("Error code " + message.substring(5));
            return false;
        }

        System.out.println("You joined the server as \"" + username + "\"");
        return true;
    }
}
