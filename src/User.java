import java.net.InetAddress;
import java.util.Calendar;

public class User {
    private String username;
    private InetAddress IP;
    private boolean isTimedOut = false;
    private Calendar calendar;

    public User(String username, InetAddress IP) {
        this.username = username;
        this.IP = IP;
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
}
