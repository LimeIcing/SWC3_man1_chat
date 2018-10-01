import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws Exception {
        int clientPort = 6951;
        List<User> users = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket receivingPacket;
        DatagramPacket sendingPacket;
        InetAddress IP = InetAddress.getByName("localhost");
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message, username = "";
        boolean userExists = false;

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());
            for (User user:users) {
                if (user.getIP().equals(receivingPacket.getAddress())) {
                    userExists  = true;
                    username = user.getUsername();
                }
            }
            if (!userExists) {
                users.add(new User(message, receivingPacket.getAddress(), clientPort));
                System.out.println("New user created: \"" + message + "\"");
                message = "J_OK";
                sendData = message.getBytes();
                sendingPacket =
                        new DatagramPacket(sendData, sendData.length, receivingPacket.getAddress(), clientPort);
                sendingSocket.send(sendingPacket);
            } else {
                message = username + ": " + message;
                System.out.println("Received message from " + message);
                sendData = message.getBytes();
                for (User user:users) {
                    sendingPacket = new DatagramPacket(sendData, sendData.length, user.getIP(), clientPort);
                    sendingSocket.send(sendingPacket);
                }
            }
        }
    }
}
