import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This is the Heartbeat class.
 * This is used by the client to tell the server if its still connected and can receive messages
 * if its not online it gets removed from the servers list
 * @see Client
 * @see TimeoutRemover
 * @author Emil, Casper
 * @version 1.0
 */
public class Heartbeat implements Runnable {
    private DatagramSocket sendingSocket;
    private InetAddress serverIP;
    private int serverPort;

    /**
     * Constructor for the Heartbeat class
     * @param sendingSocket     A socket to sent messages on
     * @param serverIP          The servers IP address
     * @param serverPort        The servers port
     */
    public Heartbeat(DatagramSocket sendingSocket, InetAddress serverIP, int serverPort) {
        this.sendingSocket = sendingSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    /**
     * @see #run()
     * #sendingPacket is the UDP packet we send to the server.
     * #sendData is a byte array, so we are able to sent our data with UDP.
     * #message is the string to store the messege. its a final since we dont want it to chanchge since the server
     *  will not know what is is then.
     */
    public void run() {
        DatagramPacket sendingPacket;
        byte[] sendData;
        final String message = "IMAV";

        while (true) {
            try {
                sendData = message.getBytes();                                                       //gets the bytes of the message
                sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort); //packs the packet with data
                sendingSocket.send(sendingPacket);                                                   //Sends the packet
            } catch (IOException iOE) {                                                              //java.oi exception
                System.out.println("Failed to send the message.");
            }
            try {
                Thread.sleep(60000);                                                             //sleeps the Thread for 60sec
            } catch (InterruptedException iE) {                                                      //InterruptedException
                iE.printStackTrace();
            }
        }
    }
}
