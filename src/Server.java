import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws Exception {
        List<String> users = new ArrayList<>();
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket receivingPacket;
        DatagramPacket sendingPacket;
        InetAddress IP = InetAddress.getByName("10.111.180.33");
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message;

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());
            System.out.println("Received message: " + message);
            sendData = message.getBytes();
            sendingPacket = new DatagramPacket(sendData, sendData.length, IP, 6951);
            sendingSocket.send(sendingPacket);
        }
    }
}
