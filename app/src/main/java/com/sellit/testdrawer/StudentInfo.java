package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

public class StudentInfo
{
    public String fullName;
    public String userName;
    public String uid;
    public String email;
    public String city;
    public String state;
    public String TAG = StudentInfo.class.getSimpleName();
    public StudentInfo()
    {

    }

    public StudentInfo(String UUID, String UserName, String FullName, String Email, String City, String State)
    {
        this.uid = UUID;
        this.userName = UserName;
        this.fullName = FullName;
        this.email = Email;
        this.city = City;
        this.state = State;
    }

    public Map<String, Object> toStudentMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("userName", userName);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("city", city);
        result.put("state", state);
        result.put("TAG", "StudentInfo");
        return result;
    }
}