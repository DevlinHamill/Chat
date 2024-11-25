package com.example.chat.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class MessagingService extends FirebaseMessagingService {
    /**
     * handles new token being created for the firebase
     * @param token The token used for sending messages to this application instance. This token is
     *     the same as the one retrieved by the firebase link.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM", "Token: "+token);
    }

    /**
     * takes care of messages that have been recieved for the fire base
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM", "460 Message: "+message.getNotification().getBody());
    }
}
