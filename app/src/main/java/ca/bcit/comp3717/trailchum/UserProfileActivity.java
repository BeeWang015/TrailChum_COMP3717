package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    Button btnSignOut;
    FirebaseUser user;

    List<AuthUI.IdpConfig> signInProviders;
    public static final int MY_REQUEST_CODE = 501;

    TextView tvName;
    TextView tvDateOfBirth;
    TextView tvGender;

    ImageView ivProfilePic;
    DatabaseReference databaseUserAccountsUserProfile;

    UserAccount userUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        ivProfilePic = findViewById(R.id.ivProfilePicUserProfile);
        btnSignOut = findViewById(R.id.btnSignOut);
        tvName = findViewById(R.id.tvNameUserProfile);
        tvDateOfBirth = findViewById(R.id.tvDOBUserProfile);
        tvGender = findViewById(R.id.tvGenderUserProfile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseUserAccountsUserProfile = FirebaseDatabase.getInstance().getReference("hikersAccounts");


        if (user != null) {

            user = FirebaseAuth.getInstance().getCurrentUser();

            tvName.setText(user.getDisplayName());
            databaseUserAccountsUserProfile.child(user.getUid()).child("dateOfBirth").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            try {
                                Log.e("TAG", "" + dataSnapshot.getValue()); // your name values you will get here
                                tvDateOfBirth.setText(dataSnapshot.getValue().toString());
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
                                tvGender.setText(dataSnapshot.getValue().toString());
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

        }


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
}


