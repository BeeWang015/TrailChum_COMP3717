package ca.bcit.comp3717.trailchum;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessengerAdapter extends RecyclerView.Adapter<MessengerAdapter.ViewHolder> {

    public static final int MESSAGE_LEFT = 0;
    public static final int MESSAGE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser userMessengerAdapter;


    public MessengerAdapter(Context mContext, List<Chat> mChat, String imageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }
    @NonNull
    @Override
    public MessengerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_RIGHT) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.user_their_messages, parent, false);
            return new MessengerAdapter.ViewHolder(v);
        } else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.messenger_chat_placeholders, parent, false);
            return new MessengerAdapter.ViewHolder(v);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessengerAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());

//        if(imageUrl.equals("default")) {
//            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            Glide.with(mContext).load(imageUrl).into(holder.profile_pic);
//        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);



        }
    }
    @Override
    public int getItemViewType(int position) {
        userMessengerAdapter = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(userMessengerAdapter.getUid())) {
            return MESSAGE_RIGHT;
        } else {
            return MESSAGE_LEFT;
        }
    }
}
