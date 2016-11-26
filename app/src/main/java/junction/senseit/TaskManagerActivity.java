package junction.senseit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskManagerActivity extends AppCompatActivity {

    private TextView tv_taskDescription;
    private TextView tv_roomNumber;
    private TextView tv_floorInformation;
    private TextView tv_location;
    private Button btn_viewOnMap;
    private Button btn_raiseIssue;
    private Button btn_acceptTask;
    private Button btn_markResolved;

    TicketInformation ticketInformation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        tv_taskDescription = (TextView) findViewById(R.id.tv_task_desc_text);
        tv_roomNumber = (TextView) findViewById(R.id.tv_room_number);
        tv_floorInformation = (TextView) findViewById(R.id.tv_floor_info);
        tv_location = (TextView)  findViewById(R.id.tv_prob_location);

        btn_viewOnMap = (Button) findViewById(R.id.btn_disp_loc_on_map);
        btn_raiseIssue = (Button) findViewById(R.id.btn_raise_issue);
        btn_acceptTask = (Button) findViewById(R.id.btn_accept_task);
        btn_markResolved = (Button) findViewById(R.id.btn_mark_resolved);

        // Get the intent object and extract the data
        Intent intent = getIntent();
        initialize( (TicketParcelable) intent.getParcelableExtra("selected_ticket"));
        attachListeners();
    }

    private void initialize(TicketParcelable ticketParcObj) {

        ticketInformation = new TicketInformation(
                ticketParcObj.getTicketID(),
                ticketParcObj.getTicketPriority(),
                ticketParcObj.getTicketDescription(),
                ticketParcObj.getLocationAddress(),
                ticketParcObj.getFloor(),
                ticketParcObj.getRoomNumber(),
                ticketParcObj.getStartTime(),
                ticketParcObj.getDeadline(),
                ticketParcObj.getTicketState()
        );

        tv_taskDescription.setText(ticketInformation.getTicketDescription());
        tv_floorInformation.setText(ticketInformation.getFloor());
        tv_roomNumber.setText(ticketInformation.getRoomNumber());
        tv_location.setText(ticketInformation.getLocationAddress());
    }

    private void attachListeners() {

        btn_viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Integrate the building map based on location
            }
        });

        btn_raiseIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: raising an issue to the boss
            }
        });

        btn_acceptTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Change the ticket status to in progress
            }
        });

        btn_markResolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Change the ticket status to resolved
            }
        });
    }
}
