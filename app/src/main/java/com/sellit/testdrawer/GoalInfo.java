package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

//Class made to describe the properties of a users goal
public class GoalInfo
{
    public String goalName;
    public String amount;
    public String uid;
    public String description;
    public String TAG = GoalInfo.class.getSimpleName();

    public GoalInfo(String UUID, String goalName, String amount, String description)
    {
        this.uid = UUID;
        this.goalName = goalName;
        this.amount = amount;
        this.description = description;
    }

    public Map<String, Object> toGoalMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("goalName", goalName);
        result.put("amount", amount);
        result.put("description", description);
        result.put("TAG", "GoalInfo");
        return result;
    }
}