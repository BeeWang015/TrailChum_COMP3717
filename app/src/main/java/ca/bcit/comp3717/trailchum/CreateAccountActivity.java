package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CreateAccountActivity extends AppCompatActivity {
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;

    EditText etFirstName;
    EditText etLastName;

    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Spinner spnrGender = findViewById(R.id.spinnerGenderCreateAccount);
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spnrGender.setAdapter(gendersAdapter);

        etEmail = findViewById(R.id.etEmailCreateAccount);

        etFirstName = findViewById(R.id.etFirstNameCreateAccount);
        etLastName = findViewById(R.id.etFirstNameCreateAccount);

        etPassword = findViewById(R.id.etPasswordCreateAccount);
        etConfirmPassword = findViewById(R.id.etConfirmPasswordCreateAccount);

        gender = spnrGender.getSelectedItem().toString();

    }

    public void onUserProfileCreated(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("firstName", etFirstName.getText().toString());
        intent.putExtra("lastName", etLastName.getText().toString());
        intent.putExtra("email", etEmail.getText().toString());
        intent.putExtra("gender", gender);

        if(etPassword == etConfirmPassword != true) {
            Toast.makeText(this, "Passwords need to be matching!",
                    Toast.LENGTH_LONG).show();
        } else {
            intent.putExtra("password", etPassword.getText().toString());
            intent.putExtra("confirmPassword", etConfirmPassword.getText().toString());
        }
        startActivity(intent);
    }

}

