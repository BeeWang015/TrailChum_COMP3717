package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class CreateAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static int MY_REQUEST_CODE = 502;
    List<AuthUI.IdpConfig> signInProviders;

    String uid;
    String email;
    String userName;
    ArrayList<String> trailsToBeDone;
    String gendersSelected;

    Button btnCreateAccount;
    Button btnSignOut;
    Button btnDatePicker;
    TextView dateOfBirthPicked;
    Spinner sGender;

    DatabaseReference databaseUserAccounts;
    FirebaseUser userCreateAccount;

    List<UserAccount> userAccountsListCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        databaseUserAccounts = FirebaseDatabase.getInstance().getReference("hikersAccounts");

        userAccountsListCreateAccount = new ArrayList<UserAccount>();
        trailsToBeDone = new ArrayList<>();
        String[] defaultTrailArray = {"555534", "555732", "553824", "553827", "555396",
                "555592", "555524", "553990", "554183", "554212"};
        Random random = new Random();
        int randInt = random.nextInt(10);
        trailsToBeDone.add(defaultTrailArray[randInt]);


//        if(databaseUserAccounts.child(userCreateAccount.getUid()).getKey()
//                == userCreateAccount.getUid()){
//            Toast.makeText(this, "User exists!", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(CreateAccountActivity.this, UserProfileActivity.class);
//            startActivity(intent);
//
//
//        }

        signInProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        showSignInOptions();

        sGender = findViewById(R.id.spinnerGenderCreateAccount);
        ArrayList<String> gendersCreateAccount = new ArrayList<>();
        gendersCreateAccount.add("Male");
        gendersCreateAccount.add("Female");
        gendersCreateAccount.add("Other");
        ArrayAdapter<String> gendersAdapterCreateAccount = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, gendersCreateAccount);

        sGender.setAdapter(gendersAdapterCreateAccount);
        gendersSelected = sGender.getSelectedItem().toString();

        btnCreateAccount = findViewById(R.id.btnCreateAccountCreateAccount);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnDatePicker = findViewById(R.id.btnDOBPickerCreateAccount);
        dateOfBirthPicked = findViewById(R.id.tvDateOfBirthCreateAccount);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTasks();
                Intent intent = new Intent(CreateAccountActivity.this, UserProfileActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("name", userName);
                intent.putExtra("dateOfBirth", dateOfBirthPicked.getText().toString());

                startActivity(intent);

            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picked");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseUserAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAccountsListCreateAccount.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    UserAccount userAccountz = studentSnapshot.getValue(UserAccount.class);
                    userAccountsListCreateAccount.add(userAccountz);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addTasks() {
        String userEmailAddTask = email;
        String nameAddTask = userName;
        String dateOfBirthAddTask = dateOfBirthPicked.getText().toString();
        String genderAddTask = gendersSelected;

        if (TextUtils.isEmpty(dateOfBirthAddTask)) {
            Toast.makeText(this, "You must enter a valid date of birth.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //String toDoTask = databaseToDoList.push().getKey();
        uid = userCreateAccount.getUid();
        UserAccount user1 = new UserAccount(uid, userEmailAddTask, nameAddTask, genderAddTask,
                dateOfBirthAddTask, trailsToBeDone);

        Task setValueToDoTask = databaseUserAccounts.child(userCreateAccount.getUid()).setValue(user1);

        setValueToDoTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(CreateAccountActivity.this,
                        "User added.", Toast.LENGTH_LONG).show();

                dateOfBirthPicked.setText("");
                sGender.setSelection(0);
            }
        });

        setValueToDoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateAccountActivity.this,
                        "something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
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

                userCreateAccount = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "" + userCreateAccount.getEmail(), Toast.LENGTH_SHORT).show();

                databaseUserAccounts.orderByChild("email").equalTo(userCreateAccount.getEmail())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Intent intent = new Intent(CreateAccountActivity.this,
                                            UserProfileActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }


                        });

                email = userCreateAccount.getEmail();
                userName = userCreateAccount.getDisplayName();

            } else {
                Toast.makeText(this, "" + response.getError().getMessage()
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDueDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

        dateOfBirthPicked.setText(currentDueDate);
    }
}

