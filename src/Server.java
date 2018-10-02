import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws Exception {
        int clientPort = 6951;
        List<User> users = new ArrayList<>();
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket receivingPacket;
        DatagramPacket sendingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message, username = "";

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

            //TODO: Handle duplicate users in JOIN
            //TODO: Handle IMAV action server-side
            //TODO: Handle QUIT action server-side. Reply to client to terminate Sender
            if (message.startsWith("/")) {
                if (message.startsWith("/JOIN ")) {
                    message = message.substring(6);
                    users.add(new User(message, receivingPacket.getAddress(), clientPort));
                    System.out.println("New user created: \"" + message + "\"");
                    message = "J_OK" + users.toString();
                    sendData = message.getBytes();
                    sendingPacket =
                            new DatagramPacket(sendData, sendData.length, receivingPacket.getAddress(), clientPort);
                    sendingSocket.send(sendingPacket);
                }

                else if (message.equals("/IMAV")) {
                }

                else if (message.equals("/QUIT")) {
                    for (User user:users) {
                        if (user.getIP().equals(receivingPacket.getAddress())) {
                            users.remove(user);
                        }
                    }
                }

                else {
                    System.out.println("BAD COMMAND \"" + message + "\"");
                }
            }

            else {
                for (User user:users) {
                    if (user.getIP().equals(receivingPacket.getAddress())) {
                        username = user.getUsername();
                    }
                }
                message = username + ": " + message;
                System.out.println("Received message from " + message);
                sendData = message.getBytes();
                for (User user:users) {
                    if (!user.getUsername().equals(username)) {
                        sendingPacket = new DatagramPacket(sendData, sendData.length, user.getIP(), clientPort);
                        sendingSocket.send(sendingPacket);
                    }
                }
                System.out.println("message sent");
            }
        }
    }
}
