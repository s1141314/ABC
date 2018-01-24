package com.example.julian.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
public class AlarmActivity extends AppCompatActivity {
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context =this;
        alarm_manager =(AlarmManager) getSystemService(ALARM_SERVICE);

        alarm_timepicker =(TimePicker) findViewById(R.id.timePicker);
        update_text =(TextView) findViewById(R.id.update_text);
        final Calendar calendar =Calendar.getInstance();
        Button alarm_on=(Button) findViewById(R.id.alarm_on);
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.set(Calendar.HOUR_OF_DAY,alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                int hour=alarm_timepicker.getHour();
                int minute=alarm_timepicker.getMinute();

                String hour_string= String.valueOf(hour);
                String minute_string= String.valueOf(minute);

                set_alarm_text("Alarm set to"+hour_string+":"+minute_string);
            }
        });
        Button alarm_off=(Button) findViewById(R.id.alarm_off);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                set_alarm_text("alarm off");
            }
        });

    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }


    private void set_alarm_text() {
    }
}

