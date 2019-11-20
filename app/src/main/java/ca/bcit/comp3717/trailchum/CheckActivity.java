package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckActivity extends AppCompatActivity {

    DatabaseReference databaseCheck;
    FirebaseUser userCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        databaseCheck = FirebaseDatabase.getInstance().getReference("hikersAccounts");
        userCheck = FirebaseAuth.getInstance().getCurrentUser();

        if (userCheck != null) {
            // User is signed in
            // Start home activity

            startActivity(new Intent(CheckActivity.this, UserProfileActivity.class));
        } else {
            // No user is signed in
            // start login activity
            startActivity(new Intent(CheckActivity.this, MainActivity.class));
        }

        // close splash activity
        finish();
    }
}

