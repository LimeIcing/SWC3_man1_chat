import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This class is responsible for packing and sending messeges from the client.
 * It implements runnable so we can use it as a Thread, so the client always is able to send messages.
 * @author Emil, Casper
 */
public class ClientSender implements Runnable {
    private DatagramSocket sendingSocket;
    private InetAddress serverIP;
    private int serverPort;
    private String username;
    /**
     * A Basic constructor
     * @param sendingSocket What socket the client is sending from
     * @param serverIP      is the servers IP
     * @param serverPort    what port on the server the packet should be send to
     * @param username      The username of the user
     */
    public ClientSender(DatagramSocket sendingSocket, InetAddress serverIP, int serverPort, String username) {
        this.sendingSocket = sendingSocket;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.username = username;
    }
    /**
     * @see #run()
     * run method is what the thread are doing
     * Input takes the input from the user.
     * sendingPacket is the UDP packet we send to the server.
     * sendData is a byte array, so we are able to sent our data with UDP.
     * message is the messes the user typed in.
     * shouldRun is to keeping the loop running so the user can keep typing messeges.
     */
    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket sendingPacket;
        byte[] sendData;
        String message;
        boolean shouldRun = true;
        /**
         * @messege quit sends a quit message to the server so it knows that the user has left. it then stops the loop
         * and exit/terminates the client program.
         */
        System.out.println(Client.colourise("The server is ready to receive your message!", "green"));
        while (shouldRun) { //makes sure the code always is running
            try {
                message = input.readLine(); //reads the input from the console

                if (message.length() > 250) { //check the length of the input, if over 250, displays a error message
                    System.out.println(Client.colourise("Message too long", "yellow"));
                } else {
                    if (message.equalsIgnoreCase("quit")) {
                        shouldRun = false;
                        message = message.toUpperCase();
                    } else { //takes the input and username and puts it into message
                        message = "DATA " + username + ": " + message;
                    }

                    /**
                     * @sendData takes the messages and converts it into bytes so it can be send
                     * @sendPacket combines the data we want to send with the server information.
                     * @send sends the new packet we have created to the socket and it gets send.
                     */
                    sendData = message.getBytes();
                    sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                    sendingSocket.send(sendingPacket);
                }
                /**
                 * @throws iOE Java.io if something goes wrong
                 */
            }  catch (IOException iOE) {
                System.out.println(Client.colourise("Failed to send the message", "red"));
            }
        }
        System.exit(0); //exit the program with code 0
    }
}
