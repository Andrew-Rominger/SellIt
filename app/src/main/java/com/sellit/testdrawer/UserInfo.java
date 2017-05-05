package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

public class UserInfo
{
    public String userName;
    public String fullName;
    public String uid;
    public String email;
    public String state;
    public String city;
    public String studentEmail;
    public String TAG = UserInfo.class.getSimpleName();
    public UserInfo()
    {

    }

    public UserInfo(String UUID, String UserName, String FullName, String Email, String studentEmail, String State, String City)
    {
        this.uid = UUID;
        this.userName = UserName;
        this.fullName = FullName;
        this.email = Email;
        this.studentEmail = studentEmail;
        this.state = State;
        this.city = City;
    }
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName", userName);
        result.put("city", city);
        result.put("state", state);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("TAG", "UserInfo");
        return result;
    }

}