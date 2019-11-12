package ca.bcit.comp3717.trailchum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrailList extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lvTrails;
    // URL to get contacts JSON
    private static String SERVICE_URL =
            "https://gis.burnaby.ca/arcgis/rest/services/OpenData/OpenData5/MapServer/14/" +
                    "query?where=1%3D1&outFields=ADDRQUAL,TRAILCLASS,STAIRS,MATERIAL,PATHNAME," +
                    "AREALEN,AREAWID,COMPKEY&outSR=4326&f=json";
    private ArrayList<Trail> trailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_list);

        trailList = new ArrayList<>();
        lvTrails = findViewById(R.id.lvTrails);
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TrailList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = null;

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(SERVICE_URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject apiMain = new JSONObject(jsonStr);
                    JSONArray trailsList = apiMain.getJSONArray("features");

                    for (int i = 0; i < trailsList.length(); i++) {
                        JSONObject listItem = trailsList.getJSONObject(i);
                        JSONObject attributes = listItem.getJSONObject("attributes");

                        if (!attributes.get("PATHNAME").toString().equals("null")) {

                            String compKey = attributes.get("COMPKEY").toString();
                            String addrqual = attributes.get("ADDRQUAL").toString();
                            String trailClass = attributes.get("TRAILCLASS").toString();
                            String width = attributes.get("AREAWID").toString();
                            String length = attributes.get("AREALEN").toString();
                            String material = attributes.get("MATERIAL").toString();
                            String stairs = attributes.get("STAIRS").toString();
                            String pathName = attributes.get("PATHNAME").toString();

                            Trail trail = new Trail();

                            trail.setPATHNAME(pathName);

                            if (!compKey.equals("null")) {
                                trail.setCOMPKEY("N/A");
                            } else {
                                trail.setCOMPKEY(compKey);
                            }

                            if (!addrqual.equals("null")) {
                                trail.setADDRQUAL("N/A");
                            } else {
                                trail.setADDRQUAL(addrqual);
                            }

                            if (!trailClass.equals("null")) {
                                trail.setTRAILCLASS("N/A");
                            } else {
                                trail.setTRAILCLASS(trailClass);
                            }

                            if (!width.equals("null")) {
                                trail.setAREAWID("N/A");
                            } else {
                                trail.setAREAWID(width);
                            }

                            if (!length.equals("null")) {
                                trail.setAREALEN("N/A");
                            } else {
                                trail.setAREALEN(length);
                            }

                            if (!material.equals("null")) {
                                trail.setMATERIAL("N/A");
                            } else {
                                trail.setMATERIAL(material);
                            }

                            if (!stairs.equals("null")) {
                                trail.setSTAIRS("N/A");
                            } else {
                                trail.setSTAIRS(stairs);
                            }

                            trailList.add(trail);

                        }

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Toon[] toonArray = toonList.toArray(new Toon[toonList.size()]);

            TrailsAdapter adapter = new TrailsAdapter(TrailList.this, trailList);

            // Attach the adapter to a ListView
            lvTrails.setAdapter(adapter);

            Toast.makeText(TrailList.this, String.valueOf(trailList.size()), Toast.LENGTH_LONG).show();
        }

    }
}

