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
            System.out.println("RAW MESSAGE: " + message);

            if (message.startsWith("JOIN ")) {
                int stop = message.indexOf(",");
                boolean userExists = false;

                if (stop == -1) {
                    username = message.substring(5);
                } else {
                    username = message.substring(5, stop);
                }

                for (User user:users) {
                    if (username.equals(user.getUsername()) || receivingPacket.getAddress().equals(user.getIP())) {
                        userExists = true;
                    }
                }

                if (userExists) {
                    message = "J_ER 401: Username or IP address is already in use";
                    sendMessage(false);
                }

                else {
                    users.add(new User(username, receivingPacket.getAddress(), receivingPacket.getPort()));
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
                    sendingPacket = new DatagramPacket(sendData, sendData.length, user.getIP(), user.getClientPort());
                    sendingSocket.send(sendingPacket);
                    System.out.println("Sent the message to " + user.getUsername() + ", " +
                            user.getIP() + ':' + user.getClientPort());
                }
            }
        }

        else {
            sendData = message.getBytes();
            sendingPacket = new DatagramPacket(
                            sendData, sendData.length, receivingPacket.getAddress(), receivingPacket.getPort());
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