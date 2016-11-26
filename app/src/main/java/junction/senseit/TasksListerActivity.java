package junction.senseit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TasksListerActivity extends AppCompatActivity {

    private ListView listView;
    private TaskListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_lister);


        listView = (ListView) findViewById(R.id.tasks_list);
        // use a linear layout manager
      /*  mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        */



    }




    class GetTicketsTask extends AsyncTask<Void, Void, Boolean> {
        private final String serverIP;
        private final int serverPort;
        private ArrayList<TicketInformation> ticketInformations = new ArrayList<>();

        public GetTicketsTask(String IP, int port){
            serverIP=IP;
            serverPort=port;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            boolean status = true;
            


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
    }



}
