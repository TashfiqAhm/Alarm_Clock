package com.example.alarmclock2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<AlarmModel> alarm_list = new ArrayList<AlarmModel>();
    private ArrayList<AlarmModel> delete_item_list = new ArrayList<AlarmModel>();
    private ReminderCustomAdapter adapter;
    private FloatingActionButton fButton;
    private int notification_id;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.alarm_list_toolbar_id);
        listView = findViewById(R.id.alarm_list_listView_id);
        fButton = findViewById(R.id.alarm_list_fab_button_id);
        setSupportActionBar(toolbar);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_alarm();

            }
        });

        listview_all_listener();





    }

    @Override
    protected void onStart() {
        super.onStart();

        load_data();
        adapter = new ReminderCustomAdapter(MainActivity.this, alarm_list);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void add_new_alarm() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.new_alarm_add_dialog, null);
        final EditText mEditText = (EditText) mview.findViewById(R.id.new_alarm_promt_editText_id);
        final Button mButton = (Button) mview.findViewById(R.id.new_alarm_promt_button_id);

        mBuilder.setView(mview);
        final AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditText.getText().toString().trim().isEmpty()) {
                    notification_id = notification_id +1;
                    notification_id = notification_id % 40000;
                    AlarmModel _alarm = new AlarmModel(mEditText.getText().toString().trim(), "", "-- : -- A.M.", "--/--/----", notification_id, false, "Alarm : OFF", 32000, 32000, 32000, 32000, 32000, false);
                    alarm_list.add(_alarm);
                    adapter.notifyDataSetChanged();
                    store_on_file();

                    Intent intent = new Intent(MainActivity.this, AlarmSet.class);
                    intent.putExtra("list", alarm_list);
                    intent.putExtra("position", alarm_list.size() - 1);
                    startActivity(intent);
                    mDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a title to your note", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void load_data() {
        String line;
        StringBuffer sb = new StringBuffer();
        alarm_list.clear();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getFilesDir() + "/tempalarm.json")));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONArray jarray = null;
        try {
            jarray = new JSONArray(sb.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
       //Log.d("jara", String.valueOf(jarray.length()));
        if (jarray != null) {
            for (int i = 0; i < jarray.length(); i++) {
                try {
                    JSONObject jobject = jarray.getJSONObject(i);


                    AlarmModel temp_alarm = new AlarmModel(
                            jobject.get("alarm_title").toString(),
                            jobject.get("alarm_description").toString(),
                            jobject.get("alarm_time").toString(),
                            jobject.get("alarm_date").toString(),
                            jobject.getInt("alarm_id"),
                            jobject.getBoolean("alarm_state"),
                            jobject.get("alarm_state_string").toString(),
                            jobject.getInt("minute"),
                            jobject.getInt("hour"),
                            jobject.getInt("day"),
                            jobject.getInt("month"),
                            jobject.getInt("year"),
                            false);

                    alarm_list.add(temp_alarm);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if(alarm_list.size() >0)
            notification_id = alarm_list.get(alarm_list.size()-1).getAlarm_id();
        else notification_id =1;

    }

    void store_on_file() {
        JSONArray json_array = new JSONArray();
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

    void listview_all_listener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AlarmSet.class);
                intent.putExtra("list", alarm_list);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });


        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(checked)
                {
                    count++;
                    mode.setTitle(count + " items selected");
                    delete_item_list.add(alarm_list.get(position));
                    AlarmModel _alarm = alarm_list.get(position);
                    _alarm.setItem_selected(true);
                    alarm_list.set(position,_alarm);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    count--;
                    mode.setTitle(count + " items selected");
                    delete_item_list.remove(alarm_list.get(position));
                    AlarmModel _alarm = alarm_list.get(position);
                    _alarm.setItem_selected(false);
                    alarm_list.set(position,_alarm);
                    adapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.listview_item_delete_menu, menu);

                fButton.setVisibility(View.GONE);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId())
                {
                    case  R.id.delete_icon_id:

                        for(AlarmModel i : delete_item_list)
                        {
                            alarm_list.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                        delete_item_list.clear();
                        Toast.makeText(MainActivity.this,count + " items deleted",Toast.LENGTH_SHORT).show();
                        count=0;
                        store_on_file();
                        mode.finish();

                        return true;

                    default:
                        return false;
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                for(AlarmModel _alarm: delete_item_list)
                {
                    int position = adapter.getPosition(_alarm);
                    _alarm.setItem_selected(false);
                    alarm_list.set(position,_alarm);
                    adapter.notifyDataSetChanged();
                }
                count=0;
                delete_item_list.clear();
                fButton.setVisibility(View.VISIBLE);

            }
        });

    }
}


