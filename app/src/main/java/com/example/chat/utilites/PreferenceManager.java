package com.example.chat.utilites;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Devlin Hamill
 * CS 460
 */

public class PreferenceManager {
    /**
     * stores all sharedPrefrences
     */
    private final SharedPreferences sharedPreferences;

    /**
     * takes in the current context of the application and sets up a specific prefrence
     * @param context the instance of the app
     */
    public PreferenceManager(Context context) {
         sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    /**
     * puts a boolean into the editor
     * @param key the specific data being put into the editor
     * @param value the true or false statement being put in to the editor
     */
    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    /**
     * gets the boolean value based on the key
     * @param key the specific data type being looked at
     * @return the boolean data from the sharedprefrences based on the key as a false
     */
    public Boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * sets the key and the value in the editor as a string value\
     * @param key the data name being put into the firebase
     * @param value the actual data being inserted into the firebase
     */
    public void putString(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    /**
     * gets the string based on the string
     * @param key the data name being searched
     * @return the data based on the key
     */
    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }

    /**
     * clears the entire editor.
     */
    public void clear(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
