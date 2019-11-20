package ca.bcit.comp3717.trailchum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MessengerActivity extends AppCompatActivity {

    FirebaseUser userMessenger;
    DatabaseReference databaseUsersMessenger;

    EditText etMessageMessenger;
    ImageButton btnSendMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        btnSendMessenger = findViewById(R.id.btnSendMessenger);
        etMessageMessenger = findViewById(R.id.etWriteMessenger);

        Intent intent = getIntent();
        final String receiverUserId = intent.getStringExtra("UID");
        userMessenger = FirebaseAuth.getInstance().getCurrentUser();

        btnSendMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMessageMessenger.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(userMessenger.getUid(), receiverUserId, msg);
                } else {
                    Toast.makeText(MessengerActivity.this, "Not sent", Toast.LENGTH_SHORT).show();
                }
                etMessageMessenger.setText("");
            }
        });


    }


    private void sendMessage(String sender, String receiver, String message) {
        databaseUsersMessenger = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseUsersMessenger.child("Chats").push().setValue(hashMap);
    }
}