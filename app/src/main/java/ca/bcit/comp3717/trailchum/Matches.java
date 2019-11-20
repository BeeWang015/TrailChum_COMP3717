package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Matches extends AppCompatActivity {

    DatabaseReference databaseUserAccountsUserProfile;
    private ProgressDialog pDialog;
    ArrayList<UserAccount> user;
    ArrayList<UserAccount> allUsers;
    ArrayList<UserAccount> matchesList;
    ListView lvUsers;
    String UID;
    FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        databaseUserAccountsUserProfile = FirebaseDatabase.getInstance().getReference("hikersAccounts");
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currUser != null) {
            UID = currUser.getUid();
        }

        lvUsers = findViewById(R.id.lvUserMatches);
        user = new ArrayList<>();
        matchesList = new ArrayList<>();
        allUsers = new ArrayList<>();
        new GetContacts().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUserAccountsUserProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    UserAccount user1 = users.getValue(UserAccount.class);
                    if (user1.getUid().equals(UID))
                        user.add(user1);
                    else
                        allUsers.add(user1);
                }

                for (UserAccount user1 : allUsers) {
                    List<String> currUserTrailList = user.get(0).getTrailsToBeDone();
                    List<String> otherUserTrailList = user1.getTrailsToBeDone();
                    boolean match = false;
                    for (int i = 0; i < currUserTrailList.size(); i++) {
                        if (match) {
                            break;
                        }
                        for (int x = 0; x <otherUserTrailList.size(); x++) {
                            if (currUserTrailList.get(i).equals(otherUserTrailList.get(x))) {
                                match = true;
                                matchesList.add(user1);
                                break;
                            }
                        }
                    }
                }

                MatchAdapter adapter = new MatchAdapter(Matches.this, matchesList);

                // Attach the adapter to a ListView
                lvUsers.setAdapter(adapter);
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
            pDialog = new ProgressDialog(Matches.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

//            for (UserAccount user1 : allUsers) {
//                List<String> currUserTrailList = user.get(0).getTrailsToBeDone();
//                List<String> otherUserTrailList = user1.getTrailsToBeDone();
//                boolean match = false;
//                for (int i = 0; i < currUserTrailList.size(); i++) {
//                    if (match) {
//                        break;
//                    }
//                    for (int x = 0; x <otherUserTrailList.size(); x++) {
//                        if (currUserTrailList.get(i).equals(otherUserTrailList.get(x))) {
//                            match = true;
//                            matchesList.add(user1);
//                            break;
//                        }
//                    }
//                }
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            MatchAdapter adapter = new MatchAdapter(Matches.this, matchesList);

            // Attach the adapter to a ListView
            lvUsers.setAdapter(adapter);
        }

    }
}
