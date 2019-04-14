package com.example.alarmclock2;

import org.json.JSONObject;

import java.io.Serializable;

public class AlarmModel implements Serializable {

    private String alarm_title;
    private String alarm_description;
    private String alarm_time;
    private String alarm_date;
    private int alarm_id;
    private String alarm_state_string;
    private boolean alarm_state;
    private int minute,hour,day,month,year;

    private boolean item_selected;

    public AlarmModel(String alarm_title, String alarm_description, String alarm_time, String alarm_date, int alarm_id, boolean alarm_state,String alarm_state_string, int minute, int hour, int day, int month, int year, boolean item_selected) {
        this.alarm_title = alarm_title;
        this.alarm_description = alarm_description;
        this.alarm_time = alarm_time;
        this.alarm_date = alarm_date;
        this.alarm_id = alarm_id;
        this.alarm_state = alarm_state;
        this.alarm_state_string = alarm_state_string;
        this.minute = minute;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.item_selected = item_selected;
    }



    public String getAlarm_state_string() {
        return alarm_state_string;
    }

    public void setAlarm_state_string(String alarm_state_string) {
        this.alarm_state_string = alarm_state_string;
    }

    public String getAlarm_title() {
        return alarm_title;
    }

    public void setAlarm_title(String alarm_title) {
        this.alarm_title = alarm_title;
    }

    public String getAlarm_description() {
        return alarm_description;
    }

    public void setAlarm_description(String alarm_description) {
        this.alarm_description = alarm_description;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public boolean isAlarm_state() {
        return alarm_state;
    }

    public void setAlarm_state(boolean alarm_state) {
        this.alarm_state = alarm_state;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isItem_selected() {
        return item_selected;
    }

    public void setItem_selected(boolean item_selected) {
        this.item_selected = item_selected;
    }

    public JSONObject getJsonObject()
    {
        JSONObject obj = new JSONObject();
        try
        {
            obj.put("alarm_title",alarm_title);
            obj.put("alarm_description",alarm_description);
            obj.put("alarm_time",alarm_time);
            obj.put("alarm_date",alarm_date);
            obj.put("alarm_id",alarm_id);
            obj.put("alarm_state_string",alarm_state_string);
            obj.put("alarm_state",alarm_state);
            obj.put("minute",minute);
            obj.put("hour",hour);
            obj.put("day",day);
            obj.put("month",month);
            obj.put("year",year);
        }
        catch (Exception e)
        {

        }
        return obj;

    }


}
