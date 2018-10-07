import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * @see Server The server class, to run the server of the chat application
 *
 */

public class Server {
    public static List<User> users = new ArrayList<>();
    private static DatagramPacket receivingPacket;
    private static String message, username = "";

    public static void main(String[] args) throws Exception {
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        byte[] receiveData = new byte[1024];
        Thread Timeout = new Thread(new TimeoutRemover());
        Timeout.start();

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

            //TODO: Handle IMAV action server-side
            if (message.startsWith("JOIN ")) {
                int stop = message.indexOf(",");
                boolean userExists = false;
                username = message.substring(5, stop);

                for (User user:users) {
                    if (username.equals(user.getUsername())) {
                        userExists = true;
                    }
                }

                if (userExists) {
                    message = "J_ER 401: Username is already in use";
                    sendMessage(false);
                }

                else {
                    users.add(new User(username, receivingPacket.getAddress()));
                    System.out.println("New user joined: \"" + username + "\"");
                    message = "J_OK";
                    sendMessage(false);
                    listUsers();
                }
            }

            else if (message.startsWith("DATA ")) {
                updateTimeout();
                System.out.println("Received message from " + message.substring(5));
                sendMessage(true);
            }

            else if (message.equals("IMAV")) {
                updateTimeout();
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

                if (users.isEmpty()) {
                    System.out.println("No users online");
                } else {
                    listUsers();
                }
            }

            else {
                message = " J_ER 501: UNKNOWN COMMAND \"" + message + "\"";
                System.out.println(message);
                sendMessage(false);
            }
        }
    }

    private static void sendMessage(boolean toAll) throws Exception {
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket sendingPacket;
        int clientPort = 6951;
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

    private static void listUsers() throws Exception{
        message = "LIST";

        for (User user:users) {
            message = message.concat(" " + user);
        }

        System.out.println("Updated user list: [" + message.substring(5) + ']');

        sendMessage(false);
        sendMessage(true);
    }

    private static void updateTimeout() {
        for (User user:users) {
            if (user.getIP().equals(receivingPacket.getAddress())) {
                user.setCalendar();
                user.setTimedOut(false);
            }
        }
    }
}