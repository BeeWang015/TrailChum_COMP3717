package ca.bcit.comp3717.trailchum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessengerActivity extends AppCompatActivity {

    FirebaseUser userMessenger;
    DatabaseReference databaseUsersMessenger;

    TextView tvUserName;

    EditText etMessageMessenger;
    ImageButton btnSendMessenger;

    MessengerAdapter messengerAdapter;
    List<Chat> mChat;

    RecyclerView rView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        btnSendMessenger = findViewById(R.id.btnSendMessenger);
        etMessageMessenger = findViewById(R.id.etWriteMessenger);
        tvUserName = findViewById(R.id.theirName);

        intent = getIntent();
        final String receiverUserId = intent.getStringExtra("receiverID");

        rView = findViewById(R.id.messages_view);
        rView.setHasFixedSize(true);
        LinearLayoutManager linLayoutManager = new LinearLayoutManager(getApplicationContext());
        linLayoutManager.setStackFromEnd(true);
        rView.setLayoutManager(linLayoutManager);

        databaseUsersMessenger = FirebaseDatabase.getInstance()
                .getReference("hikersAccounts").child(receiverUserId);
        userMessenger = FirebaseAuth.getInstance().getCurrentUser();

        readMessages(userMessenger.getUid(), receiverUserId, "");


        btnSendMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMessageMessenger.getText().toString();
                if (!msg.equals("")) {
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

    private void readMessages(final String myID, final String userID, final String imageURL) {
        mChat = new ArrayList<>();

        databaseUsersMessenger = FirebaseDatabase.getInstance().getReference("Chats");
        databaseUsersMessenger.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    mChat.add(chat);

                    messengerAdapter = new MessengerAdapter(MessengerActivity.this, mChat, imageURL);
                    rView.setAdapter(messengerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
