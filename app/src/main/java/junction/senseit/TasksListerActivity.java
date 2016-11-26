package junction.senseit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TasksListerActivity extends AppCompatActivity {

    private ListView listView;
    private TaskListAdapter listAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_lister);

        listView = (ListView) findViewById(R.id.tasks_list);
        GetTicketsTask task =  new GetTicketsTask();
        task.execute((Void) null);

        attachListeners();
    }

    private void attachListeners () {

        // Attach the listener to the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TicketInformation ticketInfo = (TicketInformation) listAdapter.getItem(position);
                Intent intent = new Intent(TasksListerActivity.this, TaskManagerActivity.class);
                intent.putExtra("selected_ticket", (Parcelable) ticketInfo);
                startActivity(intent);
            }
        });
    }

    class GetTicketsTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<TicketInformation> arrTicketInformation = null;

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean status = false;

            String data= "";
            try {

                // TODO: Invoke the backend API to get the tickets
                arrTicketInformation = createTicketFromJSON(data);
                if(arrTicketInformation != null) {

                    status = true;
                }
            }catch (JSONException ex){

                ex.printStackTrace();
            }

            return status;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {

                        listAdapter = new TaskListAdapter(TasksListerActivity.this, arrTicketInformation);
                        listView.setAdapter(listAdapter);
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Couldn't fetch ticket information", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            // Display a message to the user
            Toast.makeText(getApplicationContext(), "Fetching ticket information cancelled", Toast.LENGTH_SHORT).show();
        }

        ArrayList<TicketInformation> createTicketFromJSON(String jsonString) throws JSONException {

            ArrayList<TicketInformation> arrTickets = null;

            int ID = 0;
            int priority = 0;
            String description = "";
            String startTime="";
            String deadline="";
            String state="";
            String address="";
            String floor="";
            String room="";

            // TODO: Verify the strings with the JSON keys passed from the backend
            JSONObject data = new JSONObject(jsonString);
            JSONArray arrTicketJSONObj = data.getJSONArray("tickets");

            arrTickets = new ArrayList<>();
            for(int index = 0; index < arrTicketJSONObj.length(); index++) {

                try {

                    ID = arrTicketJSONObj.getJSONObject(index).getInt("ID");
                    priority = arrTicketJSONObj.getJSONObject(index).getInt("priority");
                    description = arrTicketJSONObj.getJSONObject(index).getString("description");
                    startTime = arrTicketJSONObj.getJSONObject(index).getString("startTime");
                    deadline = arrTicketJSONObj.getJSONObject(index).getString("deadline");
                    state = arrTicketJSONObj.getJSONObject(index).getString("state");

                    TicketInformation ticketInformation;
                    if (deadline.equals("") && state.equals("")){
                        ticketInformation =  new TicketInformation(ID, priority, description, address, floor, room, startTime);
                    }
                    else if (deadline.equals("")){
                        ticketInformation =  new TicketInformation(ID, priority, description, address, floor, room, startTime, parseState(state));
                    }
                    else if (state.equals("")){
                        ticketInformation =  new TicketInformation(ID, priority, description, address, floor, room, startTime, deadline);
                    }
                    else{
                        ticketInformation =  new TicketInformation(ID, priority, description, address, floor, room, startTime, deadline, parseState(state));
                    }

                    arrTickets.add(ticketInformation);
                }
                catch (JSONException ex) {

                    System.out.println(ex.getMessage() + " Didn't get the required parameters from backend");
                }
            }

            return  arrTickets;
        }

        private TicketInformation.TicketState parseState(String state) {

            switch (state) {
                case "not started":
                    return TicketInformation.TicketState.NOT_STARTED;

                case "in progress":
                    return TicketInformation.TicketState.IN_PROGRESS;

                case "ready":
                    return TicketInformation.TicketState.READY;

                case "problem":
                    return TicketInformation.TicketState.PROBLEM;

                default:
                    return TicketInformation.TicketState.ON_THE_WAY;
            }
        }
    }



}
