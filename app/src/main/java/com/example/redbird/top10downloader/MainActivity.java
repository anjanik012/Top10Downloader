package com.example.redbird.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listApps;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps =  findViewById(R.id.xmlListView);

        Log.d(TAG, "onCreate: starting AsyncTask");

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");

    }

    private class DownloadData extends AsyncTask<String, Void, String>{
        private static final String TAG = "DownloadData";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter here is " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this , R.layout.list_record, parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed == null){
                Log.e(TAG, "doInBackground: Error");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath){

            StringBuilder xmlResult = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                int responseCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "downloadXML: response code " + responseCode);

//                InputStream inputStream = httpURLConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];

                while(true){

                    charsRead = reader.read(inputBuffer);

                    if(charsRead < 0){
                        break;
                    }
                    if(charsRead > 0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0 , charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL");
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IOException");
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: No Internet permission");
            }

            return null;
        }

    }
}


