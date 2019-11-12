package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Spinner spnrGender;

    EditText etFirstName;
    EditText etLastName;

    TextView tvDate;

    String genderSelected;
    String dateOfBirth;

    Button btnCreateAccount;

    DatabaseReference databaseAccounts;

    List<UserAccount> userAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("Accounts");

        spnrGender = findViewById(R.id.spinnerGenderCreateAccount);
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spnrGender.setAdapter(gendersAdapter);

        etEmail = findViewById(R.id.etEmailCreateAccount);

        etFirstName = findViewById(R.id.etFirstNameCreateAccount);
        etLastName = findViewById(R.id.etLastNameCreateAccount);

        etPassword = findViewById(R.id.etPasswordCreateAccount);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etConfirmPassword = findViewById(R.id.etConfirmPasswordCreateAccount);
        etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        genderSelected = spnrGender.getSelectedItem().toString();
        userAccountList = new ArrayList<UserAccount>();

        Button btnDatePicker = findViewById(R.id.btnDOBPickerCreateAccount);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        btnCreateAccount = findViewById(R.id.btnCreateAccountCreateAccount);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserAccount();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAccountList.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    UserAccount userAcc = studentSnapshot.getValue(UserAccount.class);
                    userAccountList.add(userAcc);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addUserAccount() {
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String dateOfBirth = tvDate.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String gender = spnrGender.getSelectedItem().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You must enter an email.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "You must enter a first name.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "You must enter a last name.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "You must a valid and matching passwords.", Toast.LENGTH_LONG).show();
            return;
        }

        UserKey userID = new UserKey();

        String key = databaseAccounts.child("user").push().getKey();
        //String toDoTask = databaseToDoList.push().getKey();
        UserAccount userAccounts = new UserAccount(email, firstName, lastName,
                gender, password, confirmPassword, dateOfBirth);

        Task setValueUserAccount = databaseAccounts.child(key).setValue(userAccounts);

        setValueUserAccount.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(CreateAccountActivity.this, "User account created.",
                        Toast.LENGTH_LONG).show();

                etEmail.setText("");
                etFirstName.setText("");
                etLastName.setText("");
                etPassword.setText("");
                etConfirmPassword.setText("");
                spnrGender.setSelection(0);
            }
        });

        setValueUserAccount.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onUserProfileCreated(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords need to be matching!",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (tvDate.getText().toString() == null || tvDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Birthday must be set correctly!",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            intent.putExtra("password", etPassword.getText().toString());
            intent.putExtra("confirmPassword", etConfirmPassword.getText().toString());
        }
        intent.putExtra("firstName", etFirstName.getText().toString());
        intent.putExtra("lastName", etLastName.getText().toString());
        intent.putExtra("email", etEmail.getText().toString());
        intent.putExtra("gender", genderSelected);
        intent.putExtra("dateOfBirth", dateOfBirth);


        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        tvDate = findViewById(R.id.tvDateOfBirthCreateAccount);
        tvDate.setText(currentDate);
        dateOfBirth = tvDate.getText().toString();
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}

