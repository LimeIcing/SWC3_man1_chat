import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Heartbeat implements Runnable {
    private DatagramSocket sendingSocket;
    private InetAddress serverIP;
    private int serverPort;

    public Heartbeat(DatagramSocket sendingSocket, InetAddress serverIP, int serverPort) {
        this.sendingSocket = sendingSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void run() {
        DatagramPacket sendingPacket;
        byte[] sendData;
        final String message = "IMAV";

        while (true) {
            try {
                sendData = message.getBytes();
                sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                sendingSocket.send(sendingPacket);
            } catch (IOException iOE) {
                System.out.println("Failed to send the message.");
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException iE) {
                iE.printStackTrace();
            }
        }
    }
}
