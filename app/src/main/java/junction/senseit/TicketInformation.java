package junction.senseit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Karri on 26.11.2016.
 */

public class TicketInformation {

    private int ID;
    private int priority;
    private String description;
    private Date startTime;
    private Date deadline;
    private TicketState state;
    private String address;
    private String floor;
    private String room;

    DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss MM dd  yyyy", Locale.ENGLISH);

    public TicketInformation(int ID, int priority, String description, String address,
                                                String floor, String room, String startTime){

        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state=TicketState.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        try{

            this.startTime= dateFormat.parse(startTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime( this.startTime);
            cal.add( Calendar.DATE, 1 );
            this.deadline = cal.getTime();
        } catch (ParseException ex){

            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor,
                                                        String room, String startTime, String deadline){

        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state= TicketState.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        try{

            this.startTime= dateFormat.parse(startTime);
            this.deadline = dateFormat.parse(deadline);
        } catch (ParseException ex){

            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor,
                                                            String room, String startTime, TicketState state){

        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.state= TicketState.NOT_STARTED;
        this.address=address;
        this.floor=floor;
        this.room=room;
        this.state=state;
        try{

            this.startTime= dateFormat.parse(startTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime( this.startTime);
            cal.add( Calendar.DATE, 1 );
            this.deadline = cal.getTime();
        } catch (ParseException ex){

            ex.printStackTrace();
        }
    }

    public TicketInformation(int ID, int priority, String description, String address, String floor,
                                            String room, String startTime, String deadline, TicketState state){

        this.ID=ID;
        this.priority=priority;
        this.description=description;
        this.address=address;
        this.floor=floor;
        this.room=room;
        this.state=state;
        try{

            this.startTime= dateFormat.parse(startTime);
            this.deadline = dateFormat.parse(deadline);
        } catch (ParseException ex){

            ex.printStackTrace();
        }
    }

    public long timeToDeadline() {

        return getDateDiff(deadline, new Date(), TimeUnit.HOURS);
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {

        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public Integer getTicketID() {

        return ID;
    }

    public Integer getTicketPriority() {

        return priority;
    }

    public String getTicketDescription() {

        return description;
    }

    public String getLocationAddress() {

        return address;
    }

    public String getFloor() {

        return floor;
    }

    public String getRoomNumber() {

        return room;
    }

    public Date getStartTime() {

        return startTime;
    }

    public Date getDeadline() {

        return deadline;
    }

    public TicketState getTicketState() {

        return state;
    }

    enum TicketState {

        NOT_STARTED(0), READY(1), IN_PROGRESS(2),PROBLEM(3),ON_THE_WAY(4);

        private int numStateType;

        TicketState(int integerState) {

            numStateType = integerState;
        }

        public int getIntegerValueOfState() {

            return numStateType;
        }
    }
}


