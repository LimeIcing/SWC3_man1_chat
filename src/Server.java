import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    List<String> users = new ArrayList<>();
    DatagramSocket receivingSocket;
    DatagramSocket sendingSocket;
    DatagramPacket receivingPacket;
    DatagramPacket sendingPacket;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
}
