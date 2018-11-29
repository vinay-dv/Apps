package com.example.vinaykatariya.notifyme;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    String link="";
    String result="";
    public class DownloadTask extends AsyncTask<String ,Void ,String>
    {

        @Override
        protected String doInBackground(String... input) {
            URL url;
            HttpURLConnection urlConnection=null;
            try{
                url = new URL (input[0]);
                urlConnection=(HttpURLConnection)  url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);

                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }return result;
            }catch (Exception e){
                e.printStackTrace();
                return "failed";
            }
        }
    }
    int notificationID=0;
    public void notification(){


        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.not);
        mBuilder.setContentTitle("Notification Alert "+ notificationID + ", Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setAutoCancel(true);


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pending implicit intent to view url
        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
        resultIntent.setData(Uri.parse(link));

        PendingIntent pending = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pending);
        mNotificationManager.notify(notificationID, mBuilder.build());

        notificationID++;


    }

    int times=0;String result1="";
    public void onButtonClick(View view){
        EditText address=(EditText)findViewById(R.id.editText);
        link=((EditText) address).getText().toString();
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {

                            DownloadTask downloadTask =new DownloadTask();
                            try {
                                result1=result;
                                result =downloadTask.execute(link).get();
                                Log.i("alag",result);
                                Log.i("alag1",result1);
                                if(result1!=result){

                                    notification();
                                }
                                times++;

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0,30000);

    }
    }
