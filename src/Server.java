import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * @see Server The server class, to run the server of the chat application
 * List stores a list of all connectet and active users
 * receivingPacket is a packet to receive messeges
 * message is what the server uses to send messeges
 *
 */

//TODO: handle errors; don't throw them further
//TODO: use packages
//TODO: consider checking usernames on a hash set.
//TODO: correct heartbeat to realise that a DATA message does the same in theory
//TODO: use switch cases where it can be applied
//TODO: use lambda expressions
//TODO: make a powerpoint presentation
//TODO: error handling should be done before the switch case; the default case is a default option, NOT an error case
//TODO: consider holding commands in a hash set/map
//TODO: use StringBuilder

public class Server {
    public static List<User> users = new ArrayList<>();
    private static DatagramPacket receivingPacket;
    private static String message, username = "";

    public static void main(String[] args) throws Exception {
        DatagramSocket receivingSocket = new DatagramSocket(6950);
        byte[] receiveData = new byte[1024];
        Thread Timeout = new Thread(new TimeoutRemover());
        Timeout.start();

        System.out.println("Server ready");
        while (true) {
            receivingPacket = new DatagramPacket(receiveData, receiveData.length);
            receivingSocket.receive(receivingPacket);
            message = new String(receivingPacket.getData(), 0, receivingPacket.getLength());
            System.out.println("RAW MESSAGE: " + message);

            if (message.startsWith("JOIN ")) {                                                                          //if the message starts with JOIN this part runs
                int stop = message.indexOf(",");
                boolean userExists = false;

                if (stop == -1) {
                    username = message.substring(5);
                } else {
                    username = message.substring(5, stop);
                }

                for (User user:users) {
                    if (username.equals(user.getUsername()) || receivingPacket.getAddress().equals(user.getIP())) {     //if the list contains a username and an IP address that already is in use
                        userExists = true;                                                                              //sets the userExist to true
                    }
                }

                if (userExists) {                                                                                       //runs if the userExist is true
                    message = "J_ER 401: Username or IP address is already in use";                                     //sends the J_ER message
                    sendMessage(false);                                                                           //sends only the message to specific client
                }

                else {
                    users.add(new User(username, receivingPacket.getAddress(), receivingPacket.getPort()));             //if the name is not taken display a messege to all users
                    System.out.println("New user joined: \"" + username + "\"");                                        //the message displayed
                    message = "J_OK";                                                                                   //sends a J_OK message to the user joining
                    sendMessage(false);                                                                           //only sends to the user
                    listUsers();                                                                                        //display a list of users on the server
                }
            }

            else if (message.startsWith("DATA ")) {                                                                     //if the message starts with JOIN this part runs
                updateTimeout();                                                                                        //updates the users heartbeat time
                System.out.println("Received message from " + message.substring(5));                                    //displays the messages server side
                sendMessage(true);                                                                                //sends the message to all users
            }

            else if (message.equals("IMAV")) {                                                                          //if the message starts with IMAV this part runs
                updateTimeout();                                                                                        //updates the users heartbeat timer
            }

            else if (message.equals("QUIT")) {                                                                          //if the message starts with QUIT this part runs
                for (User user:users) {                                                                                 //starts a for each loop on the user list to find the user who send the quit
                    if (user.getIP().equals(receivingPacket.getAddress())) {                                            //finds the user by using the addresses received from the packet
                        message = "User \"" + user + "\" has left the server!";                                         //post a user has left the server
                        System.out.println(message);                                                                    //print the quit message
                        sendMessage(true);                                                                        //sends the message to all users on the server
                        users.remove(user);                                                                             //removes the user from the list
                        break;                                                                                          //breaks the if
                    }
                }

                if (users.isEmpty()) {                                                                                  //if the list is empty prints this
                    System.out.println("No users online");                                                              //displays a messages
                } else {
                    listUsers();                                                                                        //list users on the server
                }
            }

            else {
                message = " J_ER 501: UNKNOWN COMMAND \"" + message + "\"";                                             //if the server don't recognised the command used
                System.out.println(message);                                                                            //prints the message
                sendMessage(false);                                                                               //sends the message to the user
            }
        }
    }

    private static void sendMessage(boolean toAll) throws Exception {                                                   //method sendMassage
        DatagramSocket sendingSocket = new DatagramSocket();
        DatagramPacket sendingPacket;
        byte[] sendData;

        if (toAll) {                                                                                                    //if sendMassage is true, it sends the message to all connectet users in the list
            for (User user : users) {
                if (user.getIP().equals(receivingPacket.getAddress())) {
                    username = user.getUsername();
                }
            }
            sendData = message.getBytes();
            for (User user : users) {                                                                                   //foreach loop the send a message to all users
                if (!user.getUsername().equals(username)) {
                    sendingPacket = new DatagramPacket(sendData, sendData.length, user.getIP(), user.getClientPort());  //the sendingPacket with length, user IP, and clientPort
                    sendingSocket.send(sendingPacket);                                                                  //sends the packet
                    System.out.println("Sent the message to " + user.getUsername() + ", " +                             //print out the message server side
                            user.getIP() + ':' + user.getClientPort());
                }
            }
        }

        else {                                                                                                          //if the message only need to be send to one client
            sendData = message.getBytes();
            sendingPacket = new DatagramPacket(
                            sendData, sendData.length, receivingPacket.getAddress(), receivingPacket.getPort());
            sendingSocket.send(sendingPacket);
        }
    }

    private static void listUsers() throws Exception{                                                                   //method for listingUsers
        message = "LIST";                                                                                               //if the message contains LIST

        for (User user:users) {
            message = message.concat(" " + user);
        }

        System.out.println("Updated user list: [" + message.substring(5) + ']');

        sendMessage(false);
        sendMessage(true);
    }

    private static void updateTimeout() {                                                                               //method for updating the time for the users
        for (User user:users) {                                                                                         //using a foreach loop to find the user by the address
            if (user.getIP().equals(receivingPacket.getAddress())) {
                user.setCalendar();                                                                                     //sets the Calender for the user
                user.setTimedOut(false);                                                                                //sets the users timeout to false
            }
        }
    }
}