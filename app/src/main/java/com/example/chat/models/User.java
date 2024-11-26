package com.example.chat.models;

import java.io.Serializable;

/**
 * @author Devlin Hamill
 * CS 460
 */
public class User implements Serializable {
    /**
     * stores the FirstName
     */
    public String Fname;
    /**
     * store the Last name of the user
     */
    public String Lname;
    /**
     * stores the encoded image
     */
    public String image;
    /**
     * stores the email as a string
     */
    public String email;
    /**
     * keeps track of the current token
     */
    public String token;
    /**
     * keeps track of the user id
     */
    public String id;
}
