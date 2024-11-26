package com.example.chat.listeners;
import com.example.chat.models.User;

/**
 * @author Devlin Hamill
 * CS 460
 */
public interface UserListener {

    /**
     * set the onUserClick method that takes in a piece of user data
     * @param user takes in a user object
     */
    void onUserClicked(User user);
}
