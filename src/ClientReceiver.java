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

        while (shouldRun) {
            try {
                receivingPacket = new DatagramPacket(receiveData, receiveData.length);
                receivingSocket.receive(receivingPacket);
                message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

                if (message.startsWith("DATA ")) {
                    System.out.println(message.substring(5));
                }

                else if (message.startsWith("LIST ")) {
                    System.out.println("Online users: " + message.substring(5));
                }
            } catch (IOException iOE) {
                iOE.printStackTrace();
            }
        }
    }
}