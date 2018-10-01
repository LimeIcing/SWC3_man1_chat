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

    public void setUsername(String username) {
        this.username = username;
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress IP) {
        this.IP = IP;
    }

    public int getReceivingPort() {
        return receivingPort;
    }

    public void setReceivingPort(int receivingPort) {
        this.receivingPort = receivingPort;
    }
}
