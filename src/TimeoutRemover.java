import java.util.Calendar;

/**
 * This class is used to remove clients from the servers list of online users if they have not sent an "im alive message"
 * for 60 seconds.
 * We use java calender to determent time.
 * @author Emil, Casper
 * @version 1.0
 */
public class TimeoutRemover implements Runnable{

    public void run() {
        Calendar calendar; //instantiate the calender

        while (true) {
            try {
                Thread.sleep(10000); //sleeps the thread for 10 seconds.
            } catch (InterruptedException iE) {
                iE.printStackTrace();
            }

            calendar = Calendar.getInstance(); //get instance is the current time past from epohq

            for (User user : Server.users) {                                            //looks though the array of users
                if (user.isTimedOut()) {                                                //if user is timed out
                    System.out.println(user + " has timed out!");                       //print the username
                    Server.users.remove(user);                                          //removes that user from the list
                    System.out.println("Updated user list: " + Server.users);           //prints out the updated userlist
                    break;                                                              //Stops the for each loop
                } else if (calendar.getTimeInMillis() - user.getCalendar() > 90000) {
                    user.setTimedOut(true);
                    /**
                     * if the time between the users check-in time and the servers time is greater than 60 seconds
                     * the user is then set to as have timed out and will gets removed when the thread wakes up.
                     * @see Heartbeat
                     */
                }
            }
        }
    }
}