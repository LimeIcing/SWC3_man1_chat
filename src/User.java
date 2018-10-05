import java.net.InetAddress;

public class User {
    private String username;
    private InetAddress IP;
    private int receivingPort;

    public User(String username, InetAddress IP, int receivingPort) {
        this.username = username;
        this.IP = IP;
        this.receivingPort = receivingPort;
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
}
