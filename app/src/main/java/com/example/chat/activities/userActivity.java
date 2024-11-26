package com.example.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.R;
import com.example.chat.adapters.UsersAdapter;
import com.example.chat.databinding.ActivityUserBinding;
import com.example.chat.listeners.UserListener;
import com.example.chat.models.User;
import com.example.chat.utilites.Constants;
import com.example.chat.utilites.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class userActivity extends AppCompatActivity implements UserListener {
    /**
     * binds the xml to the java class
     */
    private ActivityUserBinding binding;
    /**
     * holds the current users information
     */
    private PreferenceManager preferenceManager;

    /**
     * Creates the application
     * @param savedInstanceState holds the current application instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        setListeners();
        getUsers();
    }

    /**
     * sets the listeners for the back button
     */
    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    /**
     * gets the users information
     */
    private void getUsers(){

        loading(true);
        /**
         * Holds the firebase refrence so that we can look through the data being stored
         */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult()!= null){
                        /**
                         * holds all the users into an array of objects
                         */
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            /**
                             * creates a new user object
                             */
                            User user = new User();
                            user.Fname = queryDocumentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.Lname = queryDocumentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if(users.size() > 0){
                            /**
                             * Intializes a user adapter so that we can display the data on the recycle view
                             */
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });

    }

    /**
     * displays an error message when their is no users
     */
    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Updates the progressBar if the data is fully loaded
     * @param isloading A boolean character that will be used as switch statement
     */
    private void loading(Boolean isloading){
        if(isloading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Opens up the chat activity when the chat when the user is clicked
     * @param user takes in a user object
     */
    @Override
    public void onUserClicked(User user) {

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();

    }
}