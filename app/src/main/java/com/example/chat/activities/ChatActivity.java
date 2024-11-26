package com.example.chat.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.R;
import com.example.chat.adapters.ChatAdapter;
import com.example.chat.databinding.ActivityChatBinding;
import com.example.chat.models.ChatMessage;
import com.example.chat.models.User;
import com.example.chat.utilites.Constants;
import com.example.chat.utilites.PreferenceManager;
import com.google.apphosting.datastore.testing.DatastoreTestTrace;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class ChatActivity extends AppCompatActivity {
    /**
     * sets the xml to the java file
     */
    private ActivityChatBinding binding;
    /**
     * contains the recieved user data
     */
    private User receiverUser;
    /**
     * holds a list of chatMessage objects
     */
    private List<ChatMessage> chatMessages;
    /**
     * creates a chat adapter object to connect to the recycle view for sending and recieving
     */
    private ChatAdapter chatAdapter;
    /**
     * contains the current user information
     */
    private PreferenceManager preferenceManager;
    /**
     * holds the firebase being used to obtain and set chat data
     */
    private FirebaseFirestore database;

    /**
     * creates the UI of the chat
     * @param savedInstanceState the current application state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
        setListeners();
        init();
        ListenMessage();
    }

    /**
     * intializes the reycle view holder
     */
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    /**
     * puts the sent message data into the firebase so that the recycle view can display the sent
     * message for both users
     */
    private void sendMessages(){
        HashMap<String, Object> message = new HashMap<>();

        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);

        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());

        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }

    /**
     * Listens for the current message data for both recieving and sending.
     */
    private void ListenMessage(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,
                preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);

        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,
                        receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,
                        preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    /**
     * checks if a message is added and saves the new data to the the firebase
     */
    private final EventListener<QuerySnapshot> eventListener = ((value, error) -> {
        if(error != null){
            return;
        }
        if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange:value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    /**
                     * creates a new message object to store message specific info into the firebase
                     */
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverID = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(
                            documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));

                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeChanged(chatMessages.size(), chatMessages.size());

                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);

        }
        binding.progressBar.setVisibility(View.GONE);
    });

    /**
     * returns the bitmap based on a string input
     * @param encodedImage encoded image that was stored on Firebase
     * @return returns the decode bitmap based on the current String input
     */
    private Bitmap getBitmapFromEncodedString(String encodedImage){
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Loads the reciever details from the user collection
     */
    private void loadReceiverDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.Fname+ " "+receiverUser.Lname);
    }

    /**
     * setListeners for the send button and back button
     */
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());

        binding.layoutSend.setOnClickListener(v -> sendMessages());
    }

    /**
     * Gets the date format and hours associated with it
     * @param date Date object that's being used with the hours
     * @return returns the date string with the hours
     */
    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMM dd, yyyy - hh:mm a",
                Locale.getDefault()).format(date);
    }
}