import java.net.InetAddress;
import java.util.Calendar;

/**
 * @see        User this class is what an user looks like in our system
 * username    The username of the client.
 * IP          The ip of the client.
 * isTimedOut  boolean to determent whether or not the client is online.
 * calender    To determent the clients time
 */

public class User {
    private String username;
    private InetAddress IP;
    private int clientPort;
    private boolean isTimedOut = false;
    private Calendar calendar;

    /**
     *
     * @param username
     * @param IP
     */
    public User(String username, InetAddress IP, int clientPort) {
        this.username = username;
        this.IP = IP;
        this.clientPort = clientPort;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public String toString() {
        return this.username;
    }

    public String getUsername() {
        return username;
    }

    public InetAddress getIP() {
        return IP;
    }

    public int getClientPort() {
        return clientPort;
    }

    public boolean isTimedOut() {
        return isTimedOut;
    }

    public void setTimedOut(boolean timedOut) {
        isTimedOut = timedOut;
    }

    /**
     *
     * @return calender time on milliseconds
     */
    public long getCalendar() {
        return calendar.getTimeInMillis();
    }

    /**
     * @see #setCalendar() takes the current time on the client
     */
    public void setCalendar() {
        this.calendar = Calendar.getInstance();
    }
}
