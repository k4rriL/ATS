package junction.senseit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Rakesh on 11/26/2016
 */

public class TicketParcelable implements Parcelable {

    private int ID;
    private int priority;
    private String description;
    private String startTime;
    private String deadline;
    private int state;
    private String address;
    private String floor;
    private String room;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // Write and read in the same order
        dest.writeInt(ID);
        dest.writeInt(priority);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(deadline);
        dest.writeInt(state);
        dest.writeString(address);
        dest.writeString(floor);
        dest.writeString(room);
    }

    public static final Parcelable.Creator<TicketParcelable> CREATOR = new Parcelable.Creator<TicketParcelable>() {
        public TicketParcelable createFromParcel(Parcel in) {

            return new TicketParcelable(in);
        }

        public TicketParcelable[] newArray(int size) {

            return new TicketParcelable[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private TicketParcelable(Parcel src) {

        ID = src.readInt();
        priority = src.readInt();
        description = src.readString();
        startTime = src.readString();
        deadline = src.readString();
        state = src.readInt();
        address = src.readString();
        floor = src.readString();
        room = src.readString();
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

    public String getStartTime() {

        return startTime;
    }

    public String getDeadline() {

        return deadline;
    }

    public TicketInformation.TicketState getTicketState() {

        return TicketInformation.TicketState.values()[state];
    }
}
