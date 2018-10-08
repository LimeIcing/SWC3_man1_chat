import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This is the Client Class
 * The client is used to connect to the server to chat
 * receivingSocket is what socket the client is listening on
 * sendingSocket is what socket the client sends from
 * serverIP the server IP
 * serverPort is the servers port
 * username is the username of the client
 * @author Emil, Casper
 * @version 1.0
 */
public class Client {
    private static DatagramSocket receivingSocket;
    private static DatagramSocket sendingSocket;
    private static InetAddress serverIP;
    private static int serverPort = 6950;
    private static String username;


    //TODO: When done, write a method to colour sout's (https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println#5762502)
    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));            //creates and input stream
        boolean isAccepted = false;                                                             //autenticator boolean
        receivingSocket = new DatagramSocket(6951);                                        //Sets the clients rs socket
        sendingSocket = new DatagramSocket();                                                   //creates a new ss
        serverIP = InetAddress.getByName("localhost");                                          //the severs IP address

        System.out.println(Client.colourise("Welcome to the chat!", "green"));    //Wellcome message in gren
        while (!isAccepted) {                                                                   //runs if user is OK
            System.out.print("Please type your name: ");
            username = input.readLine();                                                        //takes the input as a username

            //+[0-9]+[-]+[_]
            if (username.matches("[a-zA-Z0-9_-]+") && username.length() < 13) {           //looks at the username for wrong chars
                isAccepted = authenticate();                                                    //runs the authenticate method if username OK
            } else {                                                                            //if wrong chars in name, try again.
                System.out.println(Client.colourise("Username can only contain letters, numbers, - and _",
                        "yellow"));
            }
        }

        Thread receiverThread = new Thread(new ClientReceiver(receivingSocket));
        Thread senderThread = new Thread(new ClientSender(sendingSocket, serverIP, serverPort, username));
        Thread heartbeat = new Thread(new Heartbeat(sendingSocket, serverIP, serverPort));
        heartbeat.start();          //Starts the heartbeat thread
        receiverThread.start();     //starts the reciverThred
        senderThread.start();       //starts the senderThread
    }

    private static boolean authenticate() throws Exception {                                    //the authenticater method for checking usernames on server
        DatagramPacket sendingPacket;
        DatagramPacket receivingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message = "JOIN " + username + ", " + serverIP + ':' + serverPort;               //creates the join messege to the server with the users username

        sendData = message.getBytes();
        sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        sendingSocket.send(sendingPacket);
        receivingPacket = new DatagramPacket(receiveData, receiveData.length);
        receivingSocket.receive(receivingPacket);
        message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

        if (message.startsWith("J_ER ")) {                                                     //if you get an error message from the server, you need to try again with a new name
            System.out.println(Client.colourise("Error code " + message.substring(5), "red"));
            return false;
        }

        System.out.println(Client.colourise("You joined the server as ", "green") + '"' +
                Client.colourise(username, "blue") + '"');
        return true;
    }

    public static String colourise(String message, String colour) {                         //our colour method for showing colours in console
        switch (colour) {
            case "green":
                message = "\u001B[32m" + message;
                break;

            case "red":
                message = "\u001B[31m" + message;
                break;

            case "blue":
                message = "\u001B[34m" + message;
                break;

            case "yellow":
                message = "\u001B[33m" + message;
                break;
        }

        return message + "\u001B[0m";
    }
}
