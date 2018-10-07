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
        receivingSocket = new DatagramSocket(6951);
        sendingSocket = new DatagramSocket();
        serverIP = InetAddress.getByName("localhost");

        System.out.println(Client.colourise("Welcome to the chat!", "green"));
        while (!isAccepted) {
            System.out.print("Please type your name: ");
            username = input.readLine();

            //+[0-9]+[-]+[_]
            if (username.matches("[a-zA-Z0-9_-]+") && username.length() < 13) {
                isAccepted = authenticate();
            } else {
                System.out.println(Client.colourise("Username can only contain letters, numbers, - and _",
                        "yellow"));
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
            System.out.println(Client.colourise("Error code " + message.substring(5), "red"));
            return false;
        }

        System.out.println(Client.colourise("You joined the server as ", "green") + '"' +
                Client.colourise(username, "blue") + '"');
        return true;
    }

    public static String colourise(String message, String colour) {
        switch (colour) {
            case "green":
                message = "\u001B[32m" + message;
                break;

            case "red":
                message = "\u001B[31m" + message;
                break;

            case "blue":
                message = "\u001B[34m" + message;
                break;

            case "yellow":
                message = "\u001B[33m" + message;
                break;
        }

        return message + "\u001B[0m";
    }
}
