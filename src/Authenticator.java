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

    //TODO: Handle J_ER USER_EXISTS

}
