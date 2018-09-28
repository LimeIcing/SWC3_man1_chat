import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientSender implements Runnable {
    DatagramSocket sendingSocket;
    InetAddress IP;

    public ClientSender(DatagramSocket sendingSocket, InetAddress IP) {
        this.sendingSocket = sendingSocket;
        this.IP = IP;
    }

    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket sendingPacket;
        byte[] sendData;
        String message;
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                message = input.readLine();
                sendData = message.getBytes();
                sendingPacket = new DatagramPacket(sendData, sendData.length, IP, 6950);
                sendingSocket.send(sendingPacket);
            } catch (IOException iOE) {
                System.out.println("Failed to send the message.");
            }
        }
    }
}
