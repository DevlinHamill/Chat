package com.example.chat.utilites;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class Constants {
    /**
     * creates a global variable that holds the user table name on the firebase
     */
    public static final String KEY_COLLECTION_USERS = "User";
    /**
     * creates a global variable that contains the firstname data within the user table
     */
    public static final String KEY_FIRST_NAME = "firstname";
    /**
     * creates a global variable that contains the lastname data within the user table
     */
    public static final String KEY_LAST_NAME = "lastname";
    /**
     * creates a global variable that contains the email data within the user table
     */
    public static final String KEY_EMAIL = "email";
    /**
     * creates a global variable that contains the password data within the user table
     */
    public static final String KEY_PASSWORD = "password";
    /**
     * creates a global variable for the unique user id for each person that signed up within the firebase
     */
    public static final String KEY_USER_ID = "userid";
    /**
     * a constant global variable track that is used to track if users will be signed in or not on the firebase
     */
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    /**
     * a constant global variable track that will be used to declare chatAppPrefrences
     */
    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    /**
     * a global variable used to declare image data within the firebase
     */
    public static final String KEY_IMAGE = "image";
    /**
     * declares a global constant for an fcm token
     */
    public static final String KEY_FCM_TOKEN = "fcmToken";
    /**
     * declares a global constant for a User
     */
    public static final String KEY_USER = "User";

    /**
     * declares a global constant for a chat
     */
    public static final String KEY_COLLECTION_CHAT = "chat";

    /**
     * declares a global constant for a senderID
     */
    public static final String KEY_SENDER_ID = "senderID";
    /**
     * declares a global constant for a recieverID
     */
    public static final String KEY_RECEIVER_ID = "receiverID";

    /**
     * declares a global constant for a message
     */
    public static final String KEY_MESSAGE = "message";

    /**
     * declares a global constant for a timestamp
     */
    public static final String KEY_TIMESTAMP = "timestamp";

}
