package com.example.kushagra.a2;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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


// Main Activity class
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{
    Spinner sourceTrain;
    private String sourceTrainText;
    Spinner destinationTrain;
    public String destinationTrainText;
    TextView sourceTextView;
    TextView destinationTextView;
    String source_station;
    String destination_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference source Spinner
        Spinner sourceSpinner = (Spinner) findViewById(R.id.sourceSpinner);
        //reference destination Spinner
        Spinner destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);

        //set adapter to external resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.train_array, android.R.layout.simple_spinner_dropdown_item);
        //set adapter layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter to sourceSpinner
        sourceSpinner.setAdapter(adapter);
        destinationSpinner.setAdapter(adapter);
        sourceSpinner.setOnItemSelectedListener(this);
        destinationSpinner.setOnItemSelectedListener(this);

    }



    public void go(View view){
        // call AsynTask to perform network operation on separate thread
        sourceTextView = (TextView) findViewById(R.id.sourceTextView);
        destinationTextView = (TextView) findViewById(R.id.destinationTextView);
        source_station = sourceTrainText;
        destination_station = destinationTrainText;
        // destinationTextView.setText(destination_station);
        String url = "http://www3.septa.org/hackathon/NextToArrive/" + source_station.replace(" ", "%20") + "/" + destination_station.replace(" ", "%20");
        new HttpAsyncTask().execute(url);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";
        String result = "";
        String ORIG_TRAIN="";
        String ORIG_DEP_TIME="";
        String ORIG_ARRIV_TIME="";
        String temp = "";
        String CONN_TRAIN ="";
        String CONN_DEP_TIME ="";
        String CONN_ARRIV_TIME="";
        boolean flag = false;


        while((line = bufferedReader.readLine()) != null)
            result += line;
        try {

            JSONArray obj = new JSONArray(result);
            for (int i = 0; i < obj.length(); i++) {

                // hr_forecast to fct object
                JSONObject fct = obj.getJSONObject(i);
                ORIG_TRAIN = (String) fct.get("orig_train");
                ORIG_ARRIV_TIME = (String) fct.get("orig_arrival_time");
                ORIG_DEP_TIME = (String) fct.get("orig_departure_time");

                try {
                    if (((String) fct.get("term_train")) != null) {
                        CONN_TRAIN = (String) fct.get("term_train");
                        CONN_DEP_TIME = (String) fct.get("term_depart_time");
                        CONN_ARRIV_TIME = (String) fct.get("term_arrival_time");
                        flag = true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(flag) {
                    temp += "Train Number: " + ORIG_TRAIN + " Arrival Time: " + ORIG_ARRIV_TIME + " Departure Time: " + ORIG_DEP_TIME + " Connection Train No.: " + CONN_TRAIN + " Connection Arrival Time: " + CONN_ARRIV_TIME + " Connection Departure Time: " + CONN_DEP_TIME + " #";
                    flag = false;
                }
                else temp += "Train Number: " + ORIG_TRAIN + " Arrival Time: " + ORIG_ARRIV_TIME + " Departure Time: " + ORIG_DEP_TIME + " #";

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
            Intent i = new Intent(getApplicationContext(), MainActivity2Activity.class);
            i.putExtra("pass_result", result);
            startActivity(i);
        }
    }



    public void viewmaps(View view){
        String mapurl = "http://www3.septa.org/hackathon/TrainView/";
        new HttpAsyncTask1().execute(mapurl);

    }

    public static String GET1(String url){
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
                result = convertInputStreamToString1(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString1(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";
        String result = "";
        String LAT="";
        String LON="";
        String temp = "";
        String TRAIN_NUMBER="";

        while((line = bufferedReader.readLine()) != null)
            result += line;
        try {

            JSONArray obj = new JSONArray(result);
            for (int i = 0; i < obj.length(); i++) {


                // hr_forecast to fct object
                JSONObject fct = obj.getJSONObject(i);
                LAT = (String) fct.get("lat");
                LON = (String) fct.get("lon");
                TRAIN_NUMBER = (String) fct.get("trainno");
                temp += LAT + "#" + LON + "#" + TRAIN_NUMBER + "#";
            }

            inputStream.close();
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET1(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            i.putExtra("latlan", result);
            startActivity(i);
            //TextView a = (TextView) findViewById(R.id.testing);
            //a.setText(result);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sourceTrain = (Spinner) findViewById(R.id.sourceSpinner);
        destinationTrain = (Spinner) findViewById(R.id.destinationSpinner);
        sourceTrainText = String.valueOf(sourceTrain.getSelectedItem());
        destinationTrainText = String.valueOf(destinationTrain.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
