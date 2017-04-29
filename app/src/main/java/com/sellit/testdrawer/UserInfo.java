package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

public class UserInfo
{
    public String UserName;
    public String phoneNumber;
    public String firstName;
    public String uid;
    public String Email;
    public String state;
    public String city;
    public String lastName;
    public String TAG = UserInfo.class.getSimpleName();
    public UserInfo()
    {

    }

    public UserInfo(String UUID, String UserName, String FirstName, String PhoneNumber, String Email, String State, String City)
    {
        this.uid = UUID;
        this.UserName = UserName;
        this.firstName = FirstName;
        this.phoneNumber = PhoneNumber;
        this.Email = Email;
        this.state = State;
        this.city = City;
    }
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName", UserName);
        result.put("city", city);
        result.put("state", state);
        result.put("lastName", lastName);
        result.put("firstName", firstName);
        result.put("phoneNumber", phoneNumber);
        return result;
    }

}
