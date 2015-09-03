package com.example.kushagra.a2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// List of trains for given source and destination

public class MainActivity2Activity extends ActionBarActivity {

    String train_nuumber="";
    final static String pass_result1 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        String[] a = new String[10];
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("pass_result");
            a = value.split("#");
        }

        ListAdapter rahulList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, a);
        ListView trainlist = (ListView) findViewById(R.id.trainList);
        trainlist.setAdapter(rahulList);

        trainlist.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String train_info = String.valueOf(parent.getItemAtPosition(position));
                        String a = train_info.replace("Train Number: ", "");
                        int spacePos = a.indexOf(" ");
                        if (spacePos > 0) {
                            train_nuumber = a.substring(0, spacePos);
                        }

                        String url = "http://www3.septa.org/hackathon/RRSchedules/" + train_nuumber;
                        new HttpAsyncTask().execute(url);
                        return true;
                    }
                }
        );


    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";
        String result = "";
        String Actual_time="";
        String Actual_time2="";
        String Station="";
        String temp = "";
        String Scheduled_time="";

        while((line = bufferedReader.readLine()) != null)
            result += line;
        try {

            JSONArray obj = new JSONArray(result);


            for (int i = 0; i < obj.length(); i++) {

                // hr_forecast to fct object
                JSONObject fct = obj.getJSONObject(i);
                Actual_time = (String) fct.get("act_tm");

                if(Actual_time.compareTo("na") == 0) {
                    JSONObject fct1 = obj.getJSONObject(i - 1);
                    Station = (String) fct1.get("station");
                    Actual_time2 = (String) fct1.get("act_tm");
                    Scheduled_time = (String) fct.get("sched_tm");
                    temp = "Previous Station Departed: " + "\n" + Station + "\n" + "Actual Departure Time: "+ "\n" + Actual_time2 + "\n" + "Scheduled Departure Time: "+ "\n" + Scheduled_time;
                    break;
                }

            }

            inputStream.close();
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(getApplicationContext(), MainActivity22Activity.class);
            i.putExtra("pass_result1", result);
            startActivity(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}