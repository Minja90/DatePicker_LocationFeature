package com.etf.telekomunikacije.datepicker;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends FragmentActivity implements DateTimePicked {

    // vales from edit text fields
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_LOCATION = "eventLocation";
    public static final String EVENT_DESCRIPTION = "eventDescription";

    //Fields which describes event details
    public EditText eventNAME;
    public EditText eventLocation;
    public EditText eventDescription;

    //Button "Set Reminder"
    Button setEvent;

    //ImageButton
    ImageButton mapsButton;

    //Selected date and time
    Calendar dateAndTimeCalendar;

    //when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent otvori = getIntent();

        eventNAME = (EditText) findViewById(R.id.editText1);
        eventLocation = (EditText) findViewById(R.id.editText2);
        eventDescription = (EditText) findViewById(R.id.editText3);
        setEvent = (Button) findViewById(R.id.setEvent);


        AddData();
        locationButton();

        dateAndTimeCalendar = Calendar.getInstance();

    }


    //method which describes what will happen when we press on "Set reminder button"
    private void AddData() {

        setEvent.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        setAlarm(dateAndTimeCalendar);
                        Toast setEvent =  Toast.makeText(MainActivity.this, "Event is set", Toast.LENGTH_SHORT);
                        setEvent.show();
                    }
                }
        );

    }

    //method which describes what will happen when we press on image button
    private void locationButton() {
        mapsButton = (ImageButton) findViewById(R.id.locationButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //caching eventLocation value
                String addr = eventLocation.getText().toString();
                addr = addr.replace("","+");

                //passing to Google Maps desired location
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + addr));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }
        });
    }


    //method which call class DatePickerFragment and show the date picker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment1 = new DatePickerFragment();
        newFragment1.show(getFragmentManager(), "datePicker");

    }

    //method which call class TimePickerFragment and show the time picker
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    //Seting the alarm manager, who will send the intent to the system with all neccessary data
    public void setAlarm(Calendar dateAndTimeCalendar) {
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent.putExtra(EVENT_NAME, eventNAME.getText().toString());
        alarmIntent.putExtra(EVENT_LOCATION, eventLocation.getText().toString());
        alarmIntent.putExtra(EVENT_DESCRIPTION, eventDescription.getText().toString());

        Random generator = new Random();


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, generator.nextInt(), alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, dateAndTimeCalendar.getTimeInMillis(), pendingIntent);

    }

    //interface
    @Override
    public void onDateSelected(int year, int month, int day) {
        dateAndTimeCalendar.set(Calendar.YEAR, year);
        dateAndTimeCalendar.set(Calendar.MONTH, month);
        dateAndTimeCalendar.set(Calendar.DAY_OF_MONTH, day);
    }


    //interface
    @Override
    public void onTimeSelected(int hour, int minute) {
        dateAndTimeCalendar.set(Calendar.HOUR_OF_DAY, hour);
        dateAndTimeCalendar.set(Calendar.MINUTE, minute);
        dateAndTimeCalendar.set(Calendar.SECOND, 0);
    }

}
