package com.example.chat.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.databinding.ItemContainerReceivedMessageBinding;
import com.example.chat.databinding.ItemContainerSentMessageBinding;
import com.example.chat.databinding.ItemContainerUserBinding;
import com.example.chat.models.ChatMessage;

import java.util.List;


/**
 * @author Devlin Hamill
 * CS 460
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /**
     * store the recievers image profile as a bit map
     */
    private Bitmap receiverProfileImage;
    /**
     * contains all chatmessages
     */
    private final List<ChatMessage> chatMessages;
    /**
     * user id of the sender
     */
    private final String sendID;
    /**
     * allows for the UI to be distinguished when it comes to sending
     */
    public static final int VIEW_TYPE_SENT = 1;
    /**
     * allows for the UI to be distinguished when it comes to receiving
     */
    public static final int VIEW_TYPE_RECEIEVED =2;

    /**
     * updates the local values when the class is called
     * @param chatMessages list of chatmessages
     * @param receiverProfileImage stores the bitmap of the recievers image
     * @param sendID stores the senders id
     */
    public ChatAdapter( List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String sendID){
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.sendID = sendID;
    }

    /**
     * returns the type of recycle view object being created
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the chat bubble within the recycle view based on if it is a sender or reciever
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(ItemContainerSentMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else{
            return new RecieverMessageViewHolder(ItemContainerReceivedMessageBinding
                    .inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)== VIEW_TYPE_SENT){
            ((SentMessageViewHolder)holder).setData(chatMessages.get(position));
        }else{
            ((RecieverMessageViewHolder)holder).setData(chatMessages.get(position), receiverProfileImage);
        }
    }

    /**
     * gets the amount of chat messages being recieved
     * @return the size of the ChatMessages list
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    /**
     * checks the item type being reviewed based on its position
     * @param position position to query
     * @return the view type in the recycle view
     */
    @Override
    public int getItemViewType(int position){
        if(chatMessages.get(position).senderID.equals(sendID)){
            return VIEW_TYPE_SENT;
        }else{
            return VIEW_TYPE_RECEIEVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        /**
         * contains the sent message binding from the xml
         */
        private final ItemContainerSentMessageBinding binding;

        /**
         * creates the sent message view holder
         * @param itemContainerSentMessageBinding the current sent message binding refrence
         */
        public SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;

        }

        /**
         * updates the gui components based on the chat message data
         * @param chatMessage the chat message object
         */
        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    static class RecieverMessageViewHolder extends RecyclerView.ViewHolder{

        /**
         * creates the item reciever binding that connects the xml container with the java
         */
        private final ItemContainerReceivedMessageBinding binding;

        /**
         * Updates the binding to the container
         * @param itemContainerReceivedMessageBinding xml and java refrence for recieving messages
         */
        public RecieverMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        /**
         * updates data based on recieving a messsage
         * @param chatMessage current message being recieved
         * @param receiverProfileImage recivers profile image as a bit map
         */
        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }

    }

}
