import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static int clientPort = 6951;
    private static List<User> users = new ArrayList<>();
    private static DatagramPacket receivingPacket;
    private static String message, username = "";

    public static void main(String[] args) throws Exception {
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        byte[] receiveData = new byte[1024];

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

            //TODO: Handle IMAV action server-side
            //TODO: Limit what goes into a username
            if (message.startsWith("JOIN ")) {
                int stop = message.indexOf(",");
                boolean userExists = false;
                username = message.substring(5, stop);
                System.out.println(username);

                for (User user:users) {
                    if (username.equals(user.getUsername())) {
                        userExists = true;
                    }
                }

                if (userExists) {
                    message = "J_ER USER_EXISTS: Username is already in use";
                    sendMessage(false);
                }

                else {
                    users.add(new User(username, receivingPacket.getAddress(), clientPort));
                    System.out.println("New user joined: \"" + username + "\"");
                    message = "J_OK" + users.toString();
                    sendMessage(false);
                    message = username + " has joined the server!";
                    sendMessage(true);
                }
            }

            else if (message.equals("IMAV")) {
            }

            else if (message.equals("QUIT")) {
                for (User user:users) {
                    if (user.getIP().equals(receivingPacket.getAddress())) {
                        message = "User \"" + user + "\" has left the server!";
                        System.out.println(message);
                        sendMessage(true);
                        users.remove(user);
                        break;
                    }
                }
            }

            else if (message.startsWith("DATA ")) {
                message = username + ": " + message.substring(5);
                System.out.println("Received message from " + message);
                sendMessage(true);
            }

            else {
                message = "BAD COMMAND \"" + message + "\"";
                System.out.println(message);
                sendMessage(false);
            }
        }
    }

    private static void sendMessage(boolean toAll) throws Exception {
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket sendingPacket;
        byte[] sendData;

        if (toAll) {
            for (User user : users) {
                if (user.getIP().equals(receivingPacket.getAddress())) {
                    username = user.getUsername();
                }
            }
            sendData = message.getBytes();
            for (User user : users) {
                if (!user.getUsername().equals(username)) {
                    sendingPacket = new DatagramPacket(sendData, sendData.length, user.getIP(), clientPort);
                    sendingSocket.send(sendingPacket);
                }
            }
        }

        else {
            sendData = message.getBytes();
            sendingPacket =
                    new DatagramPacket(sendData, sendData.length, receivingPacket.getAddress(), clientPort);
            sendingSocket.send(sendingPacket);
        }
    }
}