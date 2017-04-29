package com.sellit.testdrawer;

import java.util.Date;

/**
 * Created by Andrew on 4/29/2017.
 */

public class Comment
{
    public String postID;
    public String UID;
    public String Content;
    public Date dateCreated;

    public Comment() {
    }

    public Comment(String postID, String UID, String content, Date dateCreated)
    {
        this.postID = postID;
        this.UID = UID;
        Content = content;
        this.dateCreated = dateCreated;
    }
}
