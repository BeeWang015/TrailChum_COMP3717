package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvName;

    ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ivProfilePic = findViewById(R.id.ivProfilePicUserProfile);

        Intent intent = getIntent();

        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String email = intent.getStringExtra("email");
        String gender = intent.getStringExtra("gender");

        String password = intent.getStringExtra("password");
        String confirmPassword = intent.getStringExtra("confirmPassword");

        tvName = findViewById(R.id.tvNameUserProfile);
        tvName.setText(firstName + " " + lastName);
    }
}
