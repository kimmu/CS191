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

import java.util.ArrayList;

/**
 * Created by kim on 2/17/18.
 */

public class MyParser extends AsyncTask<Void, Integer, Integer> {

     Context ctx;
    // ListView listView;
     String data;   // we shall get this from the Downloader class

     ArrayList<String> listDate = new ArrayList<String>();
     ArrayList<String> listTitle = new ArrayList<String>();
     ArrayList<String> listProf = new ArrayList<String>();
     ArrayList<String> listSubject = new ArrayList<String>();
     ArrayList<String> listTime = new ArrayList<String>();
     ArrayList<String> listDescription = new ArrayList<String>();


     ProgressDialog progressDialog;


     public MyParser(Context ctx, String data/*, ListView listView*/) {
          this.ctx = ctx;
          this.data = data;
          //this.listView = listView;
     }



     @Override
     protected void onPreExecute() {
          super.onPreExecute();
          progressDialog = new ProgressDialog(ctx);
          progressDialog.setTitle("Parser");
          progressDialog.setMessage("Please wait...");
          progressDialog.show();
     }


     @Override
     protected Integer doInBackground(Void... params) {

          return this.parse();     // remember the parse() method returns an integer --> 1 if successful. otherwise, 0
     }


     @Override
     protected void onPostExecute(Integer integer) {
          super.onPostExecute(integer);

          if (integer == 1) {
               // successful, so display things in ListView using our ArrayAdapter
               // iListView na

               Toast.makeText(ctx, "Parsing Successful", Toast.LENGTH_SHORT).show();


               /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, listTitle);      // arrayList
               listView.setAdapter(adapter);      // adapt to ListView

               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                         // pop out window later
                         Toast.makeText(ctx, "You clicked"+position, Toast.LENGTH_SHORT).show();
                    }
               });*/

          } else {
               Toast.makeText(ctx, "Unable to parse", Toast.LENGTH_SHORT).show();
          }

          progressDialog.dismiss();
     }




     // actual parsing
     private int parse() {    // will tell if parsing has been successful
          try {
               // JSONArray contains many data--> JSON objects
               JSONArray jsonArray = new JSONArray(data);   // assigned by the constructor above
               JSONObject jsonObject = null;                // create a JSON obect to hold a single item

               listTitle.clear();  // make sure to clear array list so there are no duplications
               listDate.clear();
               listProf.clear();
               listSubject.clear();
               listTime.clear();
               listDescription.clear();


               // loop through the JSON array
               for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    String temp = jsonObject.getString("title");   // for each object, get the title of event
                    listTitle.add(temp);
                    temp = jsonObject.getString("date");
                    listDate.add(temp);
                    temp = jsonObject.getString("prof");
                    listProf.add(temp);
                    temp = jsonObject.getString("subject");
                    listSubject.add(temp);
                    temp = jsonObject.getString("time");
                    listTime.add(temp);
                    temp = jsonObject.getString("description");
                    listDescription.add(temp);
               }
               return 1;      // if successful

          } catch (JSONException e) {
               e.printStackTrace();
          }
          return 0;
     }


     public ArrayList<String> getListDate() {
          return this.listDate;
     }

     public ArrayList<String> getListTitle() {
          return this.listTitle;
     }

     public ArrayList<String> getListProf() {
          return this.listProf;
     }

     public ArrayList<String> getListSubject() {
          return this.listSubject;
     }

     public ArrayList<String> getListTime() {
          return this.listTime;
     }

     public ArrayList<String> getListDescription() {
          return this.listDescription;
     }
}
