package junction.senseit;

import android.graphics.Point;

import java.sql.Time;
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

public class Ticket {
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

    public Ticket(int ID, int priority, String description, String address, String floor, String room, String startTime){
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

    public Ticket(int ID, int priority, String description, String address, String floor, String room, String startTime, String deadline){
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

    public Ticket(int ID, int priority, String description, String address, String floor, String room, String startTime, States state){
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

    public Ticket(int ID, int priority, String description, String address, String floor, String room, String startTime, String deadline, States state){
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

    Ticket createTicketFromJSON(JSONObject data){
        int ID = 0;
        int priority = 0;
        String description = "";
        String startTime="";
        String deadline="";
        String state="";
        String address="";
        String floor="";
        String room="";

        try{
            ID = data.getInt("ID");
            priority = data.getInt("priority");
            description = data.getString("description");
            startTime = data.getString("startTime");
        }
            catch (JSONException ex) {

                System.out.println(ex.getMessage() + " Didnt get the required parameters");
            }
        try {
            deadline = data.getString("deadline");
        } catch (JSONException ex){
            deadline = "";
            System.out.println(ex.getMessage() + "");
        }
        try {
            state = data.getString("state");
        } catch (JSONException ex){
            state = "";
            System.out.println(ex.getMessage() + "");
        }

        Ticket ticket;
        if (deadline == "" && state==""){
            ticket =  new Ticket(ID, priority, description, address, floor, room, startTime);
        }
        else if (deadline==""){
            ticket =  new Ticket(ID, priority, description, address, floor, room, startTime, parseState(state));
        }
        else{
            ticket =  new Ticket(ID, priority, description, address, floor, room, startTime, deadline, parseState(state));
        }

        return ticket;
    }

    public long timeToDeadline() {
        return getDateDiff(deadline, new Date(), TimeUnit.HOURS);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }





    public States parseState(String state) {
        if (state == "not started") return States.NOT_STARTED;
        else if (state == "in progress") return States.IN_PROGRESS;
        else if (state == "ready") return States.READY;
        else if (state == "problem") return States.PROBLEM;
        else return States.ON_THE_WAY;
    }





    enum States{
        NOT_STARTED, READY, IN_PROGRESS,PROBLEM,ON_THE_WAY
    }

}


