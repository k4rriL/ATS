package junction.senseit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TasksListerActivity extends AppCompatActivity {

    private ListView listView;
    private TaskListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_lister);


        listView = (ListView) findViewById(R.id.tasks_list);
        GetTicketsTask task =  new GetTicketsTask();
        task.execute((Void) null);
        // use a linear layout manager
      /*  mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        */



    }




    class GetTicketsTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<TicketInformation> ticketInformations = new ArrayList<>();
        @Override
        protected Boolean doInBackground(Void... params) {

            boolean status = true;

            String data= "";
            try {
                TicketInformation ticket = createTicketFromJSON(data);
                ticketInformations.add(ticket);
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
                        listAdapter = new TaskListAdapter(TasksListerActivity.this,ticketInformations);
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

        TicketInformation createTicketFromJSON(String  sdata) throws JSONException{
            int ID = 0;
            int priority = 0;
            String description = "";
            String startTime="";
            String deadline="";
            String state="";
            String address="";
            String floor="";
            String room="";

            JSONObject  data= new JSONObject(sdata);
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
                System.out.println(ex.getMessage() + "");
            }
            try {
                state = data.getString("state");
            } catch (JSONException ex){
                state = "";
                System.out.println(ex.getMessage() + "");
            }

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

            return ticketInformation;
        }

        public TicketInformation.States parseState(String state) {
            if (state.equals("not started")) return TicketInformation.States.NOT_STARTED;
            else if (state.equals("in progress")) return TicketInformation.States.IN_PROGRESS;
            else if (state.equals("ready")) return TicketInformation.States.READY;
            else if (state.equals("problem")) return TicketInformation.States.PROBLEM;
            else return TicketInformation.States.ON_THE_WAY;
        }



    }



}
