package com.example.chat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.databinding.ItemContainerUserBinding;
import com.example.chat.listeners.UserListener;
import com.example.chat.models.User;

import java.util.List;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
    /**
     * the list of users
     */
    private final List<User> users;
    /**
     * user listener object that will help store data
     */
    private final UserListener userListener;

    /**
     * stores info when the class is called
     * @param users list of users on the firestore database
     * @param userListener the listener when the user is being clicked
     */
    public UsersAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    /**
     * creates the recycle view object for each user being listed from the firestore database
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return user view holder
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding
                .inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new UserViewHolder(itemContainerUserBinding);
    }

    /**
     * sets the user data to the view holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));

    }

    /**
     * checks how many users are listed
     * @return returns an integer size to dictate the amount of exisiting users
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        /**
         * refrences the xml in the java file
         */
        ItemContainerUserBinding binding;

        /**
         * updates the binding to an input container binding
         * @param itemContainerUserBinding user container binding
         */
        public UserViewHolder(ItemContainerUserBinding itemContainerUserBinding) {

            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }

        /**
         * sets the user data to the gui components
         * @param user user being reviewed
         */
        void setUserData(User user){
            binding.textName.setText(user.Fname + " "+ user.Lname);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));

            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));

        }

    }

    /**
     * decodes a encoded message to a Bitmap of the user image
     * @param encodedImage user image being decoded
     * @return returns the decoded user image as a bitmap
     */
    private Bitmap getUserImage(String encodedImage){
        byte [] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
