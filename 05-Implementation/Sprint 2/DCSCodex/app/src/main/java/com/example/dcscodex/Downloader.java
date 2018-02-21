package com.example.dcscodex;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kim on 2/17/18.
 */

// on clicking syncButton, should navigate to this

public class Downloader extends AsyncTask<Void, Integer, String> {    // doInBackground, onProgressUpdate, and onPostExecute parameter types

     Context ctx;
     String json_url; // url for connection; address
     //ListView listView;
     String dataResult = null;


     ProgressDialog progressDialog;

     public Downloader(Context ctx, String json_url/*, ListView listView*/) {
          this.ctx = ctx;
          this.json_url = json_url;
          //this.listView = listView;
     }


     @Override
     protected void onPreExecute() {
          super.onPreExecute();

          progressDialog = new ProgressDialog(ctx);
          progressDialog.setTitle("Status");
          progressDialog.setMessage("Fetching data... Please wait");
          progressDialog.show();
     }


     @Override
     protected String doInBackground(Void... params) {      // string here will be passed on onPostExecute()
          String data = downloadData();
          return data;
     }


     @Override
     protected void onPostExecute(String result) {     // string from doInBackground
          super.onPostExecute(result);
          progressDialog.dismiss();

          // before parsing, make sure that data not null
          if (result != null) {
               // pass data
               //MyParser parser = new MyParser(ctx, result, listView);
               //parser.execute();

               Toast.makeText(ctx, "Sync successful", Toast.LENGTH_SHORT).show();    // this is the cut for the downloading part. now we go on to parsing
               dataResult = result;
               Toast.makeText(ctx, "dataResult in Downloader: "+dataResult, Toast.LENGTH_SHORT).show();    // this is the cut for the downloading part. now we go on to parsing


          } else {
               Toast.makeText(ctx, "Unable to download data", Toast.LENGTH_SHORT).show();
          }
     }




     // actual downloading of data
     private String downloadData() {
          // connect and get the stream of data
          InputStream inputStream = null;
          String line = null;      // store each line/row in this string

          try {
               URL url = new URL(json_url);
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
               inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
               StringBuffer stringBuffer = new StringBuffer();

               if (bufferedReader != null) {
                    while ((line = bufferedReader.readLine()) != null) {
                         stringBuffer.append(line+"\n");
                    }
               } else {
                    return  null;
               }
               return stringBuffer.toString();

          } catch (MalformedURLException e) {
               e.printStackTrace();
          } catch (IOException e) {
               e.printStackTrace();
          } finally {
               // check if inputStream is not null; for java version 6 and below
               if (inputStream != null) {
                    try {
                         inputStream.close();
                    } catch (IOException e) {
                         e.printStackTrace();
                    }
               }
          }
          return null;
     }

     public String getDataResult() {
          return this.dataResult;
     }


}
