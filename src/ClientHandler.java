import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable{
    List<User> users = new ArrayList<>();
    private DatagramSocket receivingSocket;
    private DatagramSocket sendingSocket;
    private InetAddress clientIP;
    private int clientPort;

    public ClientHandler(DatagramSocket receivingSocket, DatagramSocket sendingSocket, InetAddress clientIP, int clientPort) {
        this.receivingSocket = receivingSocket;
        this.sendingSocket = sendingSocket;
        this.clientIP = clientIP;
        this.clientPort = clientPort;
    }

    public void run() {

    }
}
