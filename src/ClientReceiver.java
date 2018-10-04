import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReceiver implements Runnable {
    private DatagramSocket receivingSocket;

    public ClientReceiver(DatagramSocket receivingSocket) {
        this.receivingSocket = receivingSocket;
    }

    public void run() {
        DatagramPacket receivingPacket;
        byte[] receiveData = new byte[1024];
        String message;
        boolean shouldRun = true;
        int userlistLength = 0;

        while (shouldRun) {
            try {
                receivingPacket = new DatagramPacket(receiveData, receiveData.length);
                receivingSocket.receive(receivingPacket);
                message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

                if (message.startsWith("J_ER ")) {
                    System.out.println("Error code " + message.substring(5));
                }

                else if (message.startsWith("DATA ")) {
                    System.out.println(message.substring(5));
                }

                else if (message.startsWith("LIST ")) {
                    if (userlistLength > 0) {
                        if (userlistLength < message.length()) {
                            System.out.println("\u001B[32mA user joined the server!\u001B[0m");
                        } else {
                            System.out.println("\u001B[32mA user left the server!\u001B[0m");
                        }
                    }

                    userlistLength = message.length();
                    message = message.substring(5).replaceAll(" ", ", ");
                    System.out.println("Online users: " + message);
                }
            } catch (IOException iOE) {
                iOE.printStackTrace();
            }
        }
    }
}