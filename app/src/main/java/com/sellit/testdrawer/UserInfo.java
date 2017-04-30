package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

public class UserInfo
{
    public String userName;
    public String phoneNumber;
    public String firstName;
    public String uid;
    public String email;
    public String state;
    public String city;
    public String TAG = UserInfo.class.getSimpleName();
    public UserInfo()
    {

    }

    public UserInfo(String UUID, String UserName, String FirstName, String PhoneNumber, String Email, String State, String City)
    {
        this.uid = UUID;
        this.userName = UserName;
        this.firstName = FirstName;
        this.phoneNumber = PhoneNumber;
        this.email = Email;
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
        result.put("firstName", firstName);
        result.put("phoneNumber", phoneNumber);
        result.put("email", email);
        result.put("TAG", "UserInfo");
        return result;
    }

}