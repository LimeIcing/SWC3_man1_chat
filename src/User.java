import java.net.InetAddress;
import java.util.Calendar;

public class User {
    private String username;
    private InetAddress IP;
    private int receivingPort;
    private boolean isTimedOut = false;
    private Calendar calendar;

    public User(String username, InetAddress IP, int receivingPort) {
        this.username = username;
        this.IP = IP;
        this.receivingPort = receivingPort;
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

    public boolean isTimedOut() {
        return isTimedOut;
    }

    public void setTimedOut(boolean timedOut) {
        isTimedOut = timedOut;
    }

    public long getCalendar() {
        return calendar.getTimeInMillis();
    }

    public void setCalendar() {
        this.calendar = Calendar.getInstance();
    }

    public int getReceivingPort() {
        return receivingPort;
    }
}
