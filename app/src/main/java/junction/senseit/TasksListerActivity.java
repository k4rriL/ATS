package junction.senseit;

import android.app.ProgressDialog;
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
    private int m_workerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_lister);

        Intent intent = getIntent();
        m_workerID = intent.getIntExtra("worker_id", -1);

        listView = (ListView) findViewById(R.id.tasks_list);
        GetTicketsTask task =  new GetTicketsTask(m_workerID);
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

        private int nWorkerID;
        private ProgressDialog progressDialog = null;
        private ArrayList<TicketInformation> arrTicketInformation = null;

        GetTicketsTask(int workerID) {

            nWorkerID = workerID;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(TasksListerActivity.this);
            progressDialog.setMessage("Retrieving tickets");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean status = false;

            String data;
            try {

                data = ((BackendConnectionHelper) getApplication()).getTicketsForWorker(nWorkerID);
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

            if(progressDialog != null && progressDialog.isShowing()) {

                progressDialog.dismiss();
            }

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

            ArrayList<TicketInformation> arrTickets;

            int ID;
            int state;
            int priority;

            String description;
            String startTime;
            String deadline = "";
            String address = "";
            String floor = "";
            String room = "";

            JSONObject data = new JSONObject(jsonString);
            data = data.getJSONObject("worker");
            JSONArray arrTicketJSONObj = data.getJSONArray("tickets");

            arrTickets = new ArrayList<>();
            for(int index = 0; index < arrTicketJSONObj.length(); index++) {

                try {

                    ID = arrTicketJSONObj.getJSONObject(index).getInt("id");
                    priority = arrTicketJSONObj.getJSONObject(index).getInt("priority");
                    description = arrTicketJSONObj.getJSONObject(index).getString("description");
                    startTime = arrTicketJSONObj.getJSONObject(index).getString("creation_time");
                    state = arrTicketJSONObj.getJSONObject(index).getInt("status");

                    TicketInformation ticketInformation;
                    if (deadline.equals("")){
                        ticketInformation =  new TicketInformation(ID, priority, description, address, floor, room, startTime, parseState(state));
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

        private TicketInformation.TicketState parseState(int state) {

            switch (state) {
                case 0:
                    return TicketInformation.TicketState.NOT_ASSIGNED;

                case 1:
                    return TicketInformation.TicketState.IN_PROGRESS;

                case 2:
                    return TicketInformation.TicketState.PROBLEM;

                case 3:
                    return TicketInformation.TicketState.RESOLVED;

                default:
                    return TicketInformation.TicketState.NOT_ASSIGNED;
            }
        }
    }



}
