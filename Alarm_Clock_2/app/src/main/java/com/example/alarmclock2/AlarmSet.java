package com.example.alarmclock2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmSet extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private Toolbar toolbar;
    private EditText alarmTitle;
    private EditText alarmDescription;
    private TextView alarmTime;
    private TextView alarmDate;
    private TextView alarmState;
    private Switch alarm_switch;
    private LinearLayout dateLayout;
    private LinearLayout timeLayout;
    private LinearLayout switchLayout;
    private FloatingActionButton fButton;
    private ArrayList<AlarmModel> alarm_list = new ArrayList<AlarmModel>();
    private int position;
    private JSONArray json_array;
    private AlarmModel _alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        toolbar = findViewById(R.id.alarm_set_toolbar_id);
        alarmTitle = findViewById(R.id.alarm_set_title_editText_id);
        alarmDescription = findViewById(R.id.alarm_set_description_editText_id);
        alarmTime = findViewById(R.id.alarm_set_time_set_linearLayout_time_id);
        alarmDate = findViewById(R.id.alarm_set_date_set_linearLayout_date_id);
        alarmState = findViewById(R.id.alarm_set_alarm_switch_state_id);
        alarm_switch = findViewById(R.id.alarm_switch_id);
        timeLayout = findViewById(R.id.alarm_set_time_set_linearLayout_id);
        dateLayout = findViewById(R.id.alarm_set_date_set_linearLayout_id);
        switchLayout = findViewById(R.id.alarm_set_alarm_switch_linearLayout_id);
        fButton = findViewById(R.id.alarm_set_fab_button_id);

        timeLayout.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        fButton.setOnClickListener(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            alarm_list = (ArrayList<AlarmModel>) getIntent().getSerializableExtra("list");
            position = (int) getIntent().getIntExtra("position", 0);
            _alarm = alarm_list.get(position);

            alarmTitle.setText(_alarm.getAlarm_title());
            alarmDescription.setText(_alarm.getAlarm_description());
            alarmTime.setText(_alarm.getAlarm_time());
            alarmDate.setText(_alarm.getAlarm_date());
            alarmState.setText(_alarm.getAlarm_state_string());
            alarm_switch.setChecked(_alarm.isAlarm_state());
        }


        alarm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //alarm main act start
                Intent intent = new Intent(AlarmSet.this,AlarmReceiver.class);
                intent.putExtra("notification_id",_alarm.getAlarm_id());
                intent.putExtra("title",_alarm.getAlarm_title());

                PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmSet.this,0,
                        intent,PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                //predeifined things for alarm

                if (isChecked == true) {
                    if (is_allowed_to_set() == true) {



                        //set alarm with the time

                        Calendar cal = Calendar.getInstance();
                        cal.set(_alarm.getYear(),_alarm.getMonth(),_alarm.getDay(),_alarm.getHour(),_alarm.getMinute(),0);
                        long alarmStartTime = cal.getTimeInMillis();

                        alarm.set(AlarmManager.RTC_WAKEUP,alarmStartTime,alarmIntent);
                        //set end



                        //if anything goes wrong delete everything above
                        alarmState.setText("Alarm : On");
                        _alarm.setAlarm_state_string("Alarm : ON");
                        _alarm.setAlarm_state(true);
                        Toast.makeText(AlarmSet.this,"Alarm : On",Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(AlarmSet.this, "Invalid Alarm Time  ", Toast.LENGTH_SHORT).show();
                        alarm_switch.setChecked(false);
                    }


                } else {
                    //alarm cancel start
                    alarm.cancel(alarmIntent);
                    //alarm cancel ends

                    alarmState.setText("Alarm : Off");
                    _alarm.setAlarm_state_string("Alarm : OFF");
                    _alarm.setAlarm_state(false);
                    Toast.makeText(AlarmSet.this,"Alarm : Off",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        store_on_file();
    }

    private void store_on_file() {
        alarm_list.get(position).setAlarm_title(alarmTitle.getText().toString());
        alarm_list.get(position).setAlarm_description(alarmDescription.getText().toString());

        json_array = new JSONArray();

        for (int i = 0; i < alarm_list.size(); i++)
            json_array.put(alarm_list.get(i).getJsonObject());

        String temp_string = json_array.toString();


        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("tempalarm.json", Context.MODE_PRIVATE);
            fos.write(temp_string.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.alarm_set_time_set_linearLayout_id && alarm_switch.isChecked() == false) {
            DialogFragment time_picker_fragment = new TimePickerFragment();
            time_picker_fragment.show(getSupportFragmentManager(), "time picker");

        } else if (v.getId() == R.id.alarm_set_date_set_linearLayout_id && alarm_switch.isChecked() == false) {
            DialogFragment date_picker_fragment = new DatePickerFragment();
            date_picker_fragment.show(getSupportFragmentManager(), "date picker");

        } /*else if (v.getId() == R.id.alarm_set_fab_button_id) {
            alarm_list.set(position, _alarm);
            store_on_file();

        }*/
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //AlarmModel _alarm = alarm_list.get(position);
        _alarm.setYear(year);
        _alarm.setMonth(month);
        _alarm.setDay(dayOfMonth);
        _alarm.setAlarm_date(dayOfMonth + "/" + (month + 1) + "/" + year);
        //alarm_list.set(position, _alarm);
        alarmDate.setText(_alarm.getAlarm_date());

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String _status = "";

        if (hourOfDay > 12)
            _status = _status + (hourOfDay - 12) + " : " + minute + " P.M.";
        else if (hourOfDay == 0)
            _status = _status + "12 : " + minute + " A.M.";
        else
            _status = _status + hourOfDay + " : " + minute + " A.M.";
        alarmTime.setText(_status);

        //AlarmModel _alarm = alarm_list.get(position);
        _alarm.setMinute(minute);
        _alarm.setHour(hourOfDay);
        _alarm.setAlarm_time(_status);
        //alarm_list.set(position, _alarm);

    }

    private boolean is_allowed_to_set() {

        Calendar cal = Calendar.getInstance();

        cal.set(_alarm.getYear(),_alarm.getMonth(),_alarm.getDay(),_alarm.getHour(),_alarm.getMinute());
        long alarmStartTime = cal.getTimeInMillis();
        long current_time = Calendar.getInstance().getTimeInMillis();

        Log.d("time show", String.valueOf(alarmStartTime));
        Log.d("time show", String.valueOf(current_time));
        Log.d("time show", String.valueOf(current_time - alarmStartTime));

        if (_alarm.getHour() != 32000 && _alarm.getMinute() != 32000 && _alarm.getDay() != 32000 && _alarm.getMonth() != 32000 && _alarm.getYear() != 32000)
            if(alarmStartTime>current_time)
                return true;

        return false;
    }
}
