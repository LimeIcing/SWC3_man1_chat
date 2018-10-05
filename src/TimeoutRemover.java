import java.util.Calendar;

public class TimeoutRemover implements Runnable{

    public void run() {
        Calendar calendar;

        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException iE) {
                iE.printStackTrace();
            }

            calendar = Calendar.getInstance();

            for (User user : Server.users) {
                if (user.isTimedOut()) {
                    System.out.println(user + " has timed out!");
                    Server.users.remove(user);
                    System.out.println("Updated user list: " + Server.users);
                    break;
                } else if (calendar.getTimeInMillis() - user.getCalendar() > 60000) {
                    user.setTimedOut(true);
                }
            }
        }
    }
}