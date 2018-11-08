package com.avi.todo.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;


import com.avi.todo.MyAssignment;
import com.avi.todo.MyTask;
import com.avi.todo.R;
import com.avi.todo.fragments.CalendarPickerFragment;
import com.avi.todo.fragments.TimeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    CalendarPickerFragment calendarPickerFragment;
    TimeFragment timeFragment;
    public ImageButton imgCalendar, imgTime;
    public EditText editWhatTodo, editDue, editTime;
    Toolbar addTool;
    Intent i;
    private static final String TAG = "AddTaskActivity";


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myReference;

    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        addTool = findViewById(R.id.addTool);
        setSupportActionBar(addTool);

        i = new Intent();

        //home button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        imgCalendar = findViewById(R.id.imageCalendar);
        imgCalendar.setOnClickListener(this);
        imgTime = findViewById(R.id.imageTime);
        imgTime.setOnClickListener(this);

        //create calender fragment
        calendarPickerFragment = new CalendarPickerFragment();
        if (calendarPickerFragment.isAdded()) {
            return;
        }


        timeFragment = new TimeFragment();
        if (timeFragment.isAdded()) {
            return;
        }

        //declare variables in oncreate
        editWhatTodo = findViewById(R.id.editWhatTodo);
        editDue = findViewById(R.id.editDue);
        editDue.setOnClickListener(this);
        editWhatTodo.setOnClickListener(this);
        editTime = findViewById(R.id.editTime);
        editTime.setOnClickListener(this);


        //work with database
        //declare the database reference object. This is what we use to access the database.

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myReference = database.getReference("Users").child(userID).child("Assignments");
       // myReference.child(userID);
       // String postId = myReference.getKey();


    }

    @Override
    public void onClick(View view) {
        new MyTask(this);
        switch (view.getId()) {
            case R.id.editDue:
            case R.id.imageCalendar:
                calendarPickerFragment.show(getSupportFragmentManager(), "calendar fragment");
                break;
            case R.id.editTime:
            case R.id.imageTime:
                timeFragment.show(getSupportFragmentManager(), "time fragment");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                String txt = editWhatTodo.getText().toString();
                String date = editDue.getText().toString();
                String time = editTime.getText().toString();
                MyAssignment assignment = new MyAssignment(txt, date,time);
                if (txt.isEmpty())
                    Toast.makeText(this, "There is no new assignment", Toast.LENGTH_LONG).show();
                else {
                    if (time.isEmpty()) {
                        assignment = new MyAssignment(txt, date);
                    }
                    //adding new assignment to database
                    myReference.push().setValue(assignment);
                    finish();

                }
                break;
        }

        //back button
        if (item.getItemId() == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        editTime.setText(hourOfDay + " : " + minute);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        editDue.setText(currentDate);
        if (!editDue.getText().toString().isEmpty()) {
            editTime.setVisibility(View.VISIBLE);
            imgTime.setVisibility(View.VISIBLE);
        }
    }
}
