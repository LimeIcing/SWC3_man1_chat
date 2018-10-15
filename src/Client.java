import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This is the Client Class
 * The client is used to connect to the server to chat
 * socket is what socket the client is listening on
 * sendingSocket is what socket the client sends from
 * serverIP the server IP
 * serverPort is the servers port
 * username is the username of the client
 * The client uses the @authenticate method to connect to the server asking if the username is taken
 * if not taken the client is accepted to the server
 * @author Emil, Casper
 * @version 1.0
 */
public class Client {
    private static DatagramSocket socket;
    private static InetAddress serverIP;
    private static int serverPort = 6950;
    private static String username;

    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));            //creates and input stream
        boolean isAccepted = false;                                                             //authenticator boolean
        socket = new DatagramSocket();                                                          //Sets the clients rs socket
        serverIP = InetAddress.getByName("localhost");                                          //the severs IP address

        System.out.println(Client.colourise("Welcome to the chat!", "green"));    //Welcome message in green
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

        Thread receiverThread = new Thread(new ClientReceiver(socket));
        Thread senderThread = new Thread(new ClientSender(socket, serverIP, serverPort, username));
        Thread heartbeat = new Thread(new Heartbeat(socket, serverIP, serverPort));
        heartbeat.start();                                                                      //Starts the heartbeat thread
        receiverThread.start();                                                                 //starts the reciverThred
        senderThread.start();                                                                   //starts the senderThread
    }

    private static boolean authenticate() throws Exception {                                    //the authenticater method for checking usernames on server
        DatagramPacket sendingPacket;
        DatagramPacket receivingPacket;
        byte[] sendData;
        byte[] receiveData = new byte[1024];
        String message = "JOIN " + username + ", " + serverIP + ':' + serverPort;               //creates the join messege to the server with the users username

        sendData = message.getBytes();
        sendingPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        socket.send(sendingPacket);
        receivingPacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivingPacket);
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
