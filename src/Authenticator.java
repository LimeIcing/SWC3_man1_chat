import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Authenticator {
    private DatagramSocket receivingSocket;
    private DatagramSocket sendingSocket;
    private InetAddress serverIP;
    private int serverPort;

    public Authenticator(DatagramSocket receivingSocket, DatagramSocket sendingSocket, InetAddress serverIP, int serverPort) {
        this.receivingSocket = receivingSocket;
        this.sendingSocket = sendingSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public boolean authenticate(String username) throws Exception {
        DatagramPacket sendingPacket;
        DatagramPacket receivingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String serverMessage;

        sendData = username.getBytes();
        sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        sendingSocket.send(sendingPacket);
        receivingPacket = new DatagramPacket(receiveData, receiveData.length);
        receivingSocket.receive(receivingPacket);
        serverMessage = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

        if (!serverMessage.equals("J_OK")) {
            System.out.println(serverMessage);
            return false;
        }
        System.out.println("You joined the server as \"" + username + "\".");
        return true;
    }
}
