package com.example.chat.activities;

import com.example.chat.utilites.PreferenceManager;
import com.example.chat.utilites.Constants;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Base64;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.R;
import com.example.chat.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class MainActivity extends AppCompatActivity {
    /**
     * contains the binding for the mainactivity page
     */
    private ActivityMainBinding binding;

    /**
     * contains the PrefrenceManger object to access the current user info
     */
    private PreferenceManager preferenceManager;

    /**
     * starts the UI
     * @param savedInstanceState uses the apps current state to start the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();

    }

    /**
     * sets the listeners for signing out and accessing a chat
     */
    private void setListeners(){
        binding.imageSignOut.setOnClickListener(v -> signOut());

        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent( getApplicationContext(), userActivity.class )));
    }

    /**
     * loads the current users information such as name and images
     */
    private void loadUserDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_FIRST_NAME)+" "+ preferenceManager.getString(Constants.KEY_LAST_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    /**
     * shows a Toast message based on the current string input
     * @param message String message that needs to be displayed
     */
    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * gets the FCM token
     */
    public void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    /**
     * updates an existing FCM token
     * @param token the new FCM token
     */
    private void updateToken(String token){
        /**
         * Keeps track of the Firestore database connection
         */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        /**
         * refrences specific data on the firebase
         */
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token update successfully"))
                .addOnFailureListener(e -> showToast("Unable to update Token"));
    }

    /**
     * signs out of the application
     */
    private void signOut(){
        showToast("Signing out....");
        /**
         * Refrences the firestore database connection
         */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        /**
         * Allows the firestore data to be read from
         */
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        /**
         * stores all updates being made
         */
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                }).addOnFailureListener(e -> showToast("Unable to sign out"));

    }

}