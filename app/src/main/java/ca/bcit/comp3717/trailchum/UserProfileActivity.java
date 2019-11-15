package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    Button btnSignOut;
    FirebaseUser user;

    List<AuthUI.IdpConfig> signInProviders;
    public static final int MY_REQUEST_CODE = 501;

    TextView tvName;

    ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();


        ivProfilePic = findViewById(R.id.ivProfilePicUserProfile);
        btnSignOut = findViewById(R.id.btnSignOut);
        tvName = findViewById(R.id.tvNameUserProfile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            user = FirebaseAuth.getInstance().getCurrentUser();

            tvName.setText(user.getDisplayName());
        } else {
            signInProviders = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

        }


    }

    public void onSignOut(View v) {
        AuthUI.getInstance().signOut(UserProfileActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                btnSignOut.setEnabled(false);
                showSignInOptions();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileActivity.this,
                        ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(signInProviders)
                        .setTheme(R.style.mySignInTheme).build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();

                tvName.setText(user.getDisplayName());

                btnSignOut.setEnabled(true);
            } else {
                Toast.makeText(this, "" + response.getError().getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

}
