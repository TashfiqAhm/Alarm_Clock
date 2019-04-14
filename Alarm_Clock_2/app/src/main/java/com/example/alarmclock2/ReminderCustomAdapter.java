package com.example.alarmclock2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderCustomAdapter extends ArrayAdapter {
    public ReminderCustomAdapter(Context context, ArrayList<AlarmModel> alarm_list)
    {
        super(context, 0, alarm_list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AlarmModel _alarm = (AlarmModel) getItem(position);
        if(convertView ==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_sample_layout, parent, false);
        }
        TextView alarmTitle = (TextView) convertView.findViewById(R.id.alarm_sample_layout_titleText_id);
        TextView alarmTime = (TextView) convertView.findViewById(R.id.alarm_sample_layout_timeText_id);
        TextView alarmDate = (TextView) convertView.findViewById(R.id.alarm_sample_layout_dateText_id);
        TextView alarmTState = (TextView) convertView.findViewById(R.id.alarm_sample_layout_alarmStateText_id);
        LinearLayout lnLayout = (LinearLayout) convertView.findViewById(R.id.alarm_sample_layout_linear_layout_id);

        lnLayout.setSelected(_alarm.isItem_selected());
        alarmTitle.setText(_alarm.getAlarm_title());
        alarmTime.setText(_alarm.getAlarm_time());
        alarmDate.setText(_alarm.getAlarm_date());
        alarmTState.setText(_alarm.getAlarm_state_string());

        return convertView;
    }
}
