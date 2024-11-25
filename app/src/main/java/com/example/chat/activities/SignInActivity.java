package com.example.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.R;
import com.example.chat.databinding.ActivitySignInBinding;
import com.example.chat.utilites.Constants;
import com.example.chat.utilites.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class SignInActivity extends AppCompatActivity {
    /**
     * allows for the xml to be attached to this document.
     */
    private ActivitySignInBinding binding;
    /**
     * creates a preferencemanager object to attach the data to the firebase properly
     */
    private PreferenceManager preferenceManager;

    /**
     * creates the UI implementation
     * @param savedInstanceState the class state that will be creating the UI
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    /**
     * declares the click listeners for each button allow for the sign in and sign out functionality to be accessed.
     */
    private void setListeners(){
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidateSignUpDetails()){
                SignIn();
            }

        });
    }

    /**
     * displays a error message so that as a toast message on the android app
     * @param message the message that will be displayed
     */
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * allows the user to sign into application if they entered an existing account
     */
    private void SignIn() {
        loading(true);
        /**
         * retrieves the firebase connection
         */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size() > 0){
                        /**
                         * Gets the documentations associated with the firebase
                         */
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME));
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        /**
                         * declares a designated activity path so that we can open the new window upon sucessful sign in
                         */
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        loading(false);
                        showToast("Unable to Sign in");

                    }
                });


    }

    /**
     * changes the state visibility of the signin button and the progress bar depending on how long
     * it takes for the firebase to find the firebase connection
     * @param isLoading a boolean character that keeps track of if the app is loading the sign in result
     */
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * checks all possible inputs to see if the inputs are valid.
     * @return the boolean result of the checks
     */
    private boolean isValidateSignUpDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please Enter your Email");
            return false;

        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Please Enter a valid Email");
            return false;

        }else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Please Enter your password");
            return false;

        }else{
            return true;
        }
    }


}