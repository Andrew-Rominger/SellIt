package com.sellit.testdrawer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/29/2017.
 */

//This is the comment class that identifies a comments property, and handles conversion from object to a HashMap.
public class Comment
{
    public String postID;
    public String uid;
    public String content;
    public Date dateCreated;

    public Comment() {
    }

    public Comment(String postID, String UID, String content, Date dateCreated)
    {
        this.postID = postID;
        this.uid = UID;
        this.content = content;
        this.dateCreated = dateCreated;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("postID", postID);
        result.put("content", content);
        result.put("dateCreated", dateCreated);
        return result;
    }
}
