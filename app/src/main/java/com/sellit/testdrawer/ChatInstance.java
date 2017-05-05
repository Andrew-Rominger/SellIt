package com.sellit.testdrawer;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jrkre on 5/4/2017.
 */

public class ChatInstance {
    public String buyer;
    public String seller;
    public String content;
    public String chatID;
    public Date dateCreated;
    public String Key;
    public String TAG = ChatInstance.class.getSimpleName();

    public ChatInstance() {
    }

    public ChatInstance(String buyer, String seller, String chatContent, Date dateCreated, String chatID) {
        this.buyer = buyer;
        this.seller = seller;
        this.content = chatContent;
        this.dateCreated = dateCreated;
        this.chatID = chatID;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("chatID", chatID);
        result.put("buyer", buyer);
        result.put("seller", seller);
        result.put("content", content);
        result.put("dateCreated", dateCreated);
        result.put("seller", seller);
        return result;
    }
}
