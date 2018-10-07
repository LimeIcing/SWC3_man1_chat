import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This class is responsible for unpacking and receiving messages from the server/clients.
 * It implements runnable so we can use it as a Thread, so the client always is able to receive messages.
 * @author Emil,Casper
 * @version 1.0
 */

public class ClientReceiver implements Runnable {
    private DatagramSocket receivingSocket;

    /**
     *
     * @param receivingSocket What socket the client wants to receive packets on.
     */
    public ClientReceiver(DatagramSocket receivingSocket) {
        this.receivingSocket = receivingSocket;
    }

    /**
     * @see #run()
     * run method is what the thread are doing.
     * Input takes the input from the user.
     * sendingPacket is the UDP packet we send to the server.
     * sendData is a byte array, so we are able to sent our data with UDP.
     * message is the messes the user typed in.
     * shouldRun is to keeping the loop running so the user can keep typing messeges.
     */
    public void run() {
        DatagramPacket receivingPacket;
        byte[] receiveData = new byte[1024];
        String message;
        int userlistLength = 0;
        boolean shouldRun = true;

        while (shouldRun) {
            try {
                //  receivingPacket is the packet of data, where receiveData is the bytes and receive.length is the length.
                //  receivingSocket is the socket the client uses to receive on. This is socket is chosen by the OS.
                //  A messeage then gets created as a string with the data from the packet, starts at index 0 in the byte array
                //      and the lenght of the array.
                receivingPacket = new DatagramPacket(receiveData, receiveData.length);
                receivingSocket.receive(receivingPacket);
                message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());

                /**
                 * This segment of the receiver reacts to the first 4 chars in message.
                 * @J_ER is when the server tells the client something is wrong. The client then prints the error code
                 *       the server sends.
                 * @DATA is the message format between server and client. where is message starts at index 5.
                 * @LIST is for printing out the online users. It tells the user of someone join or leaves the server
                 */
                if (message.startsWith("J_ER ")) {
                    System.out.println(Client.colourise("Error code " + message.substring(5), "red"));
                }

                else if (message.startsWith("DATA ")) {
                    int usernameStop = message.indexOf(":");
                    System.out.println(Client.colourise(message.substring(5, usernameStop), "blue") +
                            message.substring(usernameStop));
                }

                else if (message.startsWith("LIST ")) {
                    if (userlistLength > 0) {
                        if (userlistLength < message.length()) {
                            System.out.println(Client.colourise("A user joined the server!", "green"));
                        } else {
                            System.out.println(Client.colourise("A user left the server!", "yellow"));
                        }
                    }
                    //makes a list of online users, to show the client. for better reading regex removes space and replace with ","
                    userlistLength = message.length();
                    message = message.substring(5).replaceAll(" ", ", ");
                    System.out.println(Client.colourise("Online users: ", "green") +
                            Client.colourise(message, "blue"));
                }
                //catches an Java.IO exception
            } catch (IOException iOE) {
                iOE.printStackTrace();
            }
        }
    }
}