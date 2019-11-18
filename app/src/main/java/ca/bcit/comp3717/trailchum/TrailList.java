package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

        lvTrails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Trail trail = trailList.get(position);

                showTrailDetailView(trail.getPATHNAME(),
                        trail.getTRAILCLASS(),
                        trail.getMATERIAL(),
                        trail.getAREALEN(),
                        trail.getAREAWID(),
                        trail.getSTAIRS(),
                        "No ratings.",
                        trail.getPATHSTART(),
                        trail.getPATHEND());

            }
        });
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

                        if (!attributes.get("PATHNAME").toString().equals("null")
                                && !attributes.get("COMPKEY").toString().equals("null")) {

                            String compKey = attributes.get("COMPKEY").toString();
                            String addrqual = attributes.get("ADDRQUAL").toString();
                            String trailClass = attributes.get("TRAILCLASS").toString();
                            String width = attributes.get("AREAWID").toString();
                            String length = attributes.get("AREALEN").toString();
                            String material = attributes.get("MATERIAL").toString();
                            String stairs = attributes.get("STAIRS").toString();
                            String pathName = attributes.get("PATHNAME").toString();
                            ArrayList<Double> pathStart = new ArrayList<>();
                            ArrayList<Double> pathEnd = new ArrayList<>();

                            Trail trail = new Trail();

                            JSONObject geometry = listItem.getJSONObject("geometry");
                            JSONArray paths = geometry.getJSONArray("paths");
                            JSONArray pathsContent = paths.getJSONArray(0);
//                            for (int x = 0; x < pathsContent.length(); x++) {
//                                for (int y = 0; y < pathsContent.getJSONArray(x).length(); y++) {
//                                    geometryPath.add(pathsContent.getJSONArray(x).getDouble(y));
//                                }
//                            }

                            for (int y = 0; y < pathsContent.getJSONArray(0).length(); y++) {
                                pathStart.add(pathsContent.getJSONArray(0).getDouble(y));
                            }

                            for (int y = 0; y < pathsContent.getJSONArray(pathsContent.length() - 1).length(); y++) {
                                pathEnd.add(pathsContent.getJSONArray(pathsContent.length() - 1).getDouble(y));
                            }

                            trail.setPATHNAME(pathName);
                            trail.setCOMPKEY(compKey);
                            trail.setPATHSTART(pathStart);
                            trail.setPATHEND(pathEnd);

                            if (addrqual.equals("null")) {
                                trail.setADDRQUAL("N/A");
                            } else {
                                trail.setADDRQUAL(addrqual);
                            }

                            if (trailClass.equals("null") || length.equals("0")) {
                                trail.setTRAILCLASS("N/A");
                            } else {
                                trail.setTRAILCLASS(trailClass);
                            }

                            if (width.equals("null") || width.equals("0")) {
                                trail.setAREAWID("N/A");
                            } else {
                                trail.setAREAWID(width + " M");
                            }

                            if (length.equals("null")) {
                                trail.setAREALEN("N/A");
                            } else {
                                trail.setAREALEN(length + " M");
                            }

                            if (material.equals("null")) {
                                trail.setMATERIAL("N/A");
                            } else {
                                trail.setMATERIAL(material);
                            }

                            if (stairs.equals("null")) {
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

    private void showTrailDetailView(final String trailTitle,
                                     final String trailClass,
                                     final String trailSurface,
                                     final String trailLength,
                                     final String trailWidth,
                                     final String stairs,
                                     final String trailRating,
                                     final ArrayList<Double> pathStart,
                                     final ArrayList<Double> pathEnd) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TrailList.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.trail_detail_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView dialogTitle = dialogView.findViewById(R.id.tvTrailName);
        dialogTitle.setText(trailTitle);

        final TextView tvTrailClass = dialogView.findViewById(R.id.tvTrailClassValue);
        tvTrailClass.setText(trailClass);

        final TextView tvTrailSurface = dialogView.findViewById(R.id.tvSurfaceValue);
        tvTrailSurface.setText(trailSurface);

        final TextView tvTrailLength  = dialogView.findViewById(R.id.tvLengthValue);
        tvTrailLength.setText(trailLength);

        final TextView tvTrailWidth = dialogView.findViewById(R.id.tvTrailWidthValue);
        tvTrailWidth.setText(trailWidth);

        final TextView tvStairs = dialogView.findViewById(R.id.tvStairsValue);
        tvStairs.setText(stairs);

        final TextView tvRating = dialogView.findViewById(R.id.tvRatingValue);
        tvRating.setText(trailRating);

        final Button btnViewOnMap = dialogView.findViewById(R.id.btnViewOnMap);
        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrailList.this, TrailStartEndMap.class);
                intent.putExtra("pathStart", pathStart);
                intent.putExtra("pathEnd", pathEnd);
                startActivity(intent);
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}

