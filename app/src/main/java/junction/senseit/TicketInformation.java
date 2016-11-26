package junction.senseit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.json.*;

/**
 * Created by Karri on 26.11.2016.
 */

public class TicketInformation {
    public int ID;
    public int priority;
    public String description;
    public Date startTime;
    public Date deadline;
    public States state;
    public String address;
    public String floor;
    public String room;

    DateFormat df = new SimpleDateFormat("kk:mm:ss MM dd  yyyy", Locale.ENGLISH);

    public TicketInformation(int ID, int priority, String description, String address, String floor, String room, String startTime){
        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state=States.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        try{

            this.startTime=df.parse(startTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime( this.startTime);
            cal.add( Calendar.DATE, 1 );
            this.deadline = cal.getTime();
        } catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor, String room, String startTime, String deadline){
        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state=States.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        try{
            this.startTime=df.parse(startTime);
            this.deadline = df.parse(deadline);
        } catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor, String room, String startTime, States state){
        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state=States.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        this.state=state;
        try{
            this.startTime=df.parse(startTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime( this.startTime);
            cal.add( Calendar.DATE, 1 );
            this.deadline = cal.getTime();
        } catch (ParseException ex){
            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor, String room, String startTime, String deadline, States state){
        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.address=address;
        this.floor=floor;
        this.room=room;
        this.state=state;
        try{
            this.startTime=df.parse(startTime);
            this.deadline = df.parse(deadline);
        } catch (ParseException ex){
            ex.printStackTrace();
        }


    }



    public long timeToDeadline() {
        return getDateDiff(deadline, new Date(), TimeUnit.HOURS);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    enum States{
        NOT_STARTED, READY, IN_PROGRESS,PROBLEM,ON_THE_WAY
    }

}


