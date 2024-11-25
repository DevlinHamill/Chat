package com.example.chat.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat.R;
import com.example.chat.databinding.ActivitySignUpBinding;
import com.example.chat.utilites.Constants;
import com.example.chat.utilites.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class SignUpActivity extends AppCompatActivity {
    /**
     * binds the xml data to the java file
     */
    private ActivitySignUpBinding binding;
    /**
     * declares a prefrence manager so that data can be stored correctly in the firebase
     */
    private PreferenceManager preferenceManager;
    /**
     * will hold the encoded image data
     */
    private String encodeImage;

    /**
     * creates the UI
     * @param savedInstanceState holds the current state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();
    }

    /**
     * declares click listeners for certain components on the app such as buttons
     */
    private void setListeners(){
        binding.textSignIn.setOnClickListener(v-> onBackPressed());

        binding.buttonSignUp.setOnClickListener(v->{
            if(isValidateSignUpDetails()){
                SignUp();
            }
        });

        binding.layoutImage.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);

        });

    }

    /**
     * shows an error messages to the user displayed as a toast
     * @param message the error message being displayed
     */
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * holds all sign up functionality so that the user can sucessfully add their account to the firebase
     */
    private void SignUp(){
        //check loading
        loading(true);

        //Post to Firebase
        /**
         * contants the firebase connection
         */
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        /**
         * holds all data on the firebase as a hashmap
         */
        HashMap<String, String> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodeImage);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {

                 loading(false);

                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.inputFirstName.getText().toString());
                preferenceManager.putString(Constants.KEY_LAST_NAME, binding.inputLastName.getText().toString());
                preferenceManager.putString(Constants.KEY_IMAGE, encodeImage);
                    /**
                     * This will be used to designate the certain activity the sign up page will be opening after a sucessful sign up
                     */
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                }).addOnFailureListener(exception ->{
                    loading(false);
                    showToast(exception.getMessage());
                });

    }

    /**
     * Encodes the image so that it can be transmited into the firebase
     * @param bitmap takes in a bitmap being used for a the image encoding
     * @return returns the string that contains the encoded image
     */
    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth / bitmap.getWidth();
        /**
         * creates a temporary bitmap
         */
        Bitmap previewBitmap = bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);

        /**
         * declares an output stream to that will be used to transmit said bitmap result as a string
         */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    /**
     * launches the new activity after a sucessful sign up
     */
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode() == RESULT_OK){
                    Uri imageUri = result.getData().getData();
                    try{
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        binding.imageProfile.setImageBitmap(bitmap);
                        binding.textAddImage.setVisibility(View.GONE);
                        encodeImage = encodeImage(bitmap);

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }

    );

    /**
     * checks if the user inputs are valid
     * @return returns the outcomes of said checks
     */
    private Boolean isValidateSignUpDetails(){
        if(encodeImage == null){
            showToast("Please select your image");
            return false;
        }else if(binding.inputFirstName.getText().toString().trim().isEmpty()) {
            showToast("Please Enter your First Name");
            return false;

        }else if(binding.inputLastName.getText().toString().trim().isEmpty()){
            showToast("Please Enter your Last Name");
            return false;

        }else if (binding.inputEmail.getText().toString().trim().isEmpty()){
            showToast("Please Enter your Email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Please Enter a valid Email");
            return false;
        }else if(binding.inputPassword.getText().toString().trim().isEmpty()) {

            showToast("Please Enter your password");
            return false;

        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {

            showToast("Please confirm your Password");
            return false;

        }else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Password & Confirm Password must be the same");
            return false;
        }else {
            return true;
        }
    }

    /**
     * updates the visiability of GUI components based on if its loading
     * @param isLoading the boolean switch that tells if its loading or not.
     */
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }

    }
}