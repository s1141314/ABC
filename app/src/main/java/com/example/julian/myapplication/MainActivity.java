package com.example.julian.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.lang.String;

public class MainActivity extends Activity {

    private static final String TAG = "ASYNC_TASK";

    private Button execute;
    private Button cancel;
    private ProgressBar progressBar;
    private TextView textView;

    private MyTask mTask;
    private JSONObject fda;
    private JSONObject fda2;
    private JSONObject sideEffect;
    private String[] b;
    private Button alarm;
    private EditText name;
    private TextView test;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText)findViewById(R.id.name);
        test = (TextView)findViewById(R.id.textview2);
        execute = (Button) findViewById(R.id.execute);
        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask = new MyTask();
                String a = name.getText().toString();
               a = a.replaceAll(" ", "%20").toUpperCase();
                test.setText(a);

                mTask.execute("https://api.fda.gov/drug/event.json?api_key=ZuVPhVJw87WtoZOw3R4oYDyI330341kKt9GEWTke&search=DrugName=["+a+"]&count=patient.reaction.reactionmeddrapt.exact");

                execute.setEnabled(false);
                cancel.setEnabled(true);
            }
        });
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTask.cancel(true);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.text_view);
        alarm = (Button) findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openAlarm();
            }
        });

    }

    public void openAlarm(){
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute() called");
            textView.setText("loading....");
        }


        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground(Params... params) called");
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse response = client.execute(get);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    long total = entity.getContentLength();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int count = 0;
                    int length = -1;
                    while ((length = is.read(buf)) != -1) {
                        baos.write(buf, 0, length);
                        count += length;

                        publishProgress((int) ((count / (float) total) * 100));

                        Thread.sleep(500);
                    }
                    return new String(baos.toByteArray(), "gb2312");
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
            progressBar.setProgress(progresses[0]);
            textView.setText("loading..." + progresses[0] + "%");
        }


        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute(Result result) called");
            String [] z;

            try {

               fda = new JSONObject(result);
                   JSONArray params = fda.getJSONArray("results");
                   //for (int i =0 ;i<params.length();i++){
                       //sideEffect = params.getJSONObject(i);
                       //String a = sideEffect.toString();
                       //b[i] = a;
                   //}
                   sideEffect = params.getJSONObject(0);
                

                Log.d("My App", fda.toString());

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            }



            String a = sideEffect.toString();


                    textView.setText(a);

            execute.setEnabled(true);
            cancel.setEnabled(false);
        }


        @Override
        public void onCancelled() {
            Log.i(TAG, "onCancelled() called");
            textView.setText("cancelled");

            progressBar.setProgress(0);

            execute.setEnabled(true);
            cancel.setEnabled(false);
        }
    }
}

