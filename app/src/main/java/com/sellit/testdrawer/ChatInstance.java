package com.sellit.testdrawer;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jrkre on 5/4/2017.
 */

public class ChatInstance {
    public String buyerUID;
    public String sellerUID;
    public String chatContent;
    public String chatID;
    public Date messageDate;
    public String Key;
    public String TAG = ChatInstance.class.getSimpleName();

    public ChatInstance() {
    }

    public ChatInstance(String buyerUID, String sellerUID, String chatContent, Date messageDate, String chatID) {
        this.buyerUID = buyerUID;
        this.sellerUID = sellerUID;
        this.chatContent = chatContent;
        this.messageDate = messageDate;
        this.chatID = chatID;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("chatID", chatID);
        result.put("buyer", buyerUID);
        result.put("seller",sellerUID);
        result.put("content", chatContent);
        result.put("dateCreated", messageDate);
        result.put("seller",sellerUID);
        return result;
    }
}
