package com.sellit.testdrawer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 4/28/2017.
 */

public class GoalInfo
{
    public String goalName;
    public String amount;
    public String uid;
    public String startDate;
    public String endDate;
    public String TAG = GoalInfo.class.getSimpleName();

    public GoalInfo(String UUID, String goalName, String amount, String startDate, String endDate)
    {
        this.uid = UUID;
        this.goalName = goalName;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Map<String, Object> toGoalMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("goalName", goalName);
        result.put("amount", amount);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("TAG", "GoalInfo");
        return result;
    }
}