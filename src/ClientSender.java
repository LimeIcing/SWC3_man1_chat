import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientSender implements Runnable {
    private DatagramSocket sendingSocket;
    private InetAddress serverIP;
    private int serverPort;

    public ClientSender(DatagramSocket sendingSocket, InetAddress serverIP, int serverPort) {
        this.sendingSocket = sendingSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket sendingPacket;
        byte[] sendData;
        String message;
        boolean shouldRun = true;
        boolean commandIsBad;

        //TODO: Make sure '/quit' command is uppercase before sending and terminate afterwards
        System.out.println("The server is ready to receive your message!");
        while (shouldRun) {
            commandIsBad = false;
            try {
                message = input.readLine();
                if (message.startsWith("/")) {
                    if (message.equalsIgnoreCase("/quit")) {
                        shouldRun = false;
                    } else {
                        commandIsBad = true;
                        System.out.println("Unknown command \"" + message + "\"");
                    }
                }

                if (!commandIsBad) {
                    sendData = message.getBytes();
                    sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                    sendingSocket.send(sendingPacket);
                }
            } catch (IOException iOE) {
                System.out.println("Failed to send the message.");
            }
        }
        System.exit(0);
    }
}
