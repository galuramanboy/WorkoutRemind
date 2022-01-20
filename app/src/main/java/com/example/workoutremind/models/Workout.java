package com.example.workoutremind.models;

public class Workout implements Comparable<Workout>{
    private String plan;
    private String day;
    private boolean notify;
    private String userId;
    private int hour;
    private int minute;

    public Workout(){}

    public Workout(String day, String plan, boolean notify, String userId, int hour, int minute){
        this.day = day;
        this.plan = plan;
        this.notify = notify;
        this.userId = userId;
        this.hour = hour;
        this.minute = minute;
    }

    public String getDay() {
        return day;
    }

    public String getPlan() {
        return plan;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isNotify() {
        return notify;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @Override
    public int compareTo(Workout w) {
        if (this.getDay().equals("Monday"))
            return -1;
        if (this.getDay().equals("Tuesday") && !w.getDay().equals("Monday"))
            return -1;
        if (this.getDay().equals("Wednesday") && !w.getDay().equals("Monday") && !w.getDay().equals("Tuesday"))
            return -1;
        if (this.getDay().equals("Thursday") && !w.getDay().equals("Monday") && !w.getDay().equals("Tuesday") && !w.getDay().equals("Wednesday"))
            return -1;
        if (this.getDay().equals("Friday") && !w.getDay().equals("Monday") && !w.getDay().equals("Tuesday") && !w.getDay().equals("Wednesday") && !w.getDay().equals("Thursday"))
            return -1;
        if (this.getDay().equals("Saturday") && !w.getDay().equals("Monday") && !w.getDay().equals("Tuesday") && !w.getDay().equals("Wednesday") && !w.getDay().equals("Thursday") && !w.getDay().equals("Friday"))
            return -1;
        if (this.getDay().equals("Sunday") && !w.getDay().equals("Monday") && !w.getDay().equals("Tuesday") && !w.getDay().equals("Wednesday") && !w.getDay().equals("Thursday") && !w.getDay().equals("Friday") && !w.getDay().equals("Saturday"))
            return -1;
        return 0;
    }
}
