package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btnSignOut;
    FirebaseUser user;
    String UID;
    ListView lvTrailsToDo;
    private static String SERVICE_URL =
            "https://gis.burnaby.ca/arcgis/rest/services/OpenData/OpenData5/MapServer/14/" +
                    "query?where=1%3D1&outFields=ADDRQUAL,TRAILCLASS,STAIRS,MATERIAL,PATHNAME," +
                    "AREALEN,AREAWID,COMPKEY&outSR=4326&f=json";
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    ArrayList<Trail> trailsToDo;
    ArrayList<UserAccount> currUser;
    ArrayList<String> trailsInDB;


    List<AuthUI.IdpConfig> signInProviders;
    public static final int MY_REQUEST_CODE = 501;

    TextView tvNameUserProfile;
    TextView tvDOBUserProfile;
    TextView tvGenderUserProfile;

    ImageView ivProfilePic;
    DatabaseReference databaseUserAccountsUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        databaseUserAccountsUserProfile = FirebaseDatabase.getInstance().getReference("hikersAccounts");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = user.getUid();
        }

        trailsToDo = new ArrayList<>();
        currUser = new ArrayList<>();
        trailsInDB = new ArrayList<>();
        lvTrailsToDo = findViewById(R.id.lvUpcomingHikesUserProfile);
        new UserProfileActivity.GetContacts().execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//
//        Fragment fragment = new TrailFragment();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.content_frame, fragment);
//        ft.commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivProfilePic = findViewById(R.id.ivProfilePicUserProfile);
        btnSignOut = findViewById(R.id.btnSignOut);
        tvNameUserProfile = findViewById(R.id.tvNameUserProfile);
        tvDOBUserProfile = findViewById(R.id.tvDOBUserProfile);
        tvGenderUserProfile = findViewById(R.id.tvGenderUserProfile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            tvNameUserProfile.setText(user.getDisplayName());

            databaseUserAccountsUserProfile.child(user.getUid()).child("dateOfBirth").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            try {
                                Log.e("TAG", "" + dataSnapshot.getValue()); // your name values you will get here
                                tvDOBUserProfile.setText(dataSnapshot.getValue().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("TAG", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onCancelled", " cancelled");
                }
            });

            databaseUserAccountsUserProfile.child(user.getUid()).child("gender").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            try {
                                Log.e("TAG", "" + dataSnapshot.getValue()); // your name values you will get here
                                tvGenderUserProfile.setText(dataSnapshot.getValue().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("TAG", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onCancelled", " cancelled");
                }
            });
        } else {
//            signInProviders = Arrays.asList(
//                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                    new AuthUI.IdpConfig.PhoneBuilder().build(),
//                    new AuthUI.IdpConfig.FacebookBuilder().build(),
//                    new AuthUI.IdpConfig.GoogleBuilder().build());
            Toast.makeText(this, "Nothing happening", Toast.LENGTH_SHORT).show();

        }

        lvTrailsToDo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trail trail = trailsToDo.get(i);
                showTrailToBeDoneDialog(trail.getPATHNAME(), trail.getCOMPKEY());
            }
        });

    }

    public void onSignOut(View v) {
        AuthUI.getInstance().signOut(UserProfileActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btnSignOut.setEnabled(false);
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileActivity.this,
                        "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = null;

        switch(id) {
            case R.id.nav_matching:
                intent = new Intent(this, Matches.class);
                break;
            case R.id.nav_mainpage:
                intent = new Intent(this,UserProfileActivity.class);
                break;
            case R.id.nav_trails:
                intent = new Intent(this, TrailList.class);
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUserAccountsUserProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                trailsToDo.clear();
//                trailsInDB.clear();
//                currUser.clear();
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    UserAccount user1 = users.getValue(UserAccount.class);
                    if (user1.getUid().equals(UID)) {
                        currUser.add(user1);
                        for (String compkey : user1.getTrailsToBeDone()) {
                            trailsInDB.add(compkey);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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
            pDialog = new ProgressDialog(UserProfileActivity.this);
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

                        for (String compKey : trailsInDB) {
                            if (attributes.get("COMPKEY").toString().equals(compKey)) {
                                Trail trail = new Trail();
                                trail.setPATHNAME(attributes.get("PATHNAME").toString());
                                trail.setCOMPKEY(attributes.get("COMPKEY").toString());
                                trailsToDo.add(trail);
                            }
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

            TrailsToDoAdapter adapter = new TrailsToDoAdapter(UserProfileActivity.this, trailsToDo);

            // Attach the adapter to a ListView
            lvTrailsToDo.setAdapter(adapter);
        }

    }

    private void showTrailToBeDoneDialog(final String pathname, final String compkey) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UserProfileActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.trail_to_be_done_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView trailName = dialogView.findViewById(R.id.tvTrailName);
        trailName.setText(pathname);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTrail(compkey);
                alertDialog.dismiss();
            }
        });

    }

    public void deleteTrail(String COMPKEY) {
        boolean exists = false;
        UserAccount currentUser = currUser.get(0);
        List<String> userTrailList = currentUser.getTrailsToBeDone();
        for (String key : userTrailList) {
            if (key.equals(COMPKEY)) {
                exists = true;
                break;
            }
        }
        if (exists) {
            userTrailList.remove(COMPKEY);
            updateUser(currentUser.getUid(),
                    currentUser.getEmail(),
                    currentUser.getName(),
                    currentUser.getGender(),
                    currentUser.getDateOfBirth(),
                    userTrailList);
        } else {
            Toast.makeText(UserProfileActivity.this, "Something went wrong.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUser(String uid, String email, String name, String gender, String dob, List<String> trailList) {

        DatabaseReference dbRef = databaseUserAccountsUserProfile.child(UID);

        UserAccount updatedUser = new UserAccount(uid, email, name, gender, dob, trailList);

        Task setValueTask = dbRef.setValue(updatedUser);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UserProfileActivity.this,
                        "Trail deleted",Toast.LENGTH_SHORT).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileActivity.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}


