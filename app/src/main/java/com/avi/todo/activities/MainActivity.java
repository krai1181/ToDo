package com.avi.todo.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.avi.todo.MyAssignment;
import com.avi.todo.MyTask;
import com.avi.todo.R;
import com.avi.todo.adapter.MyRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public RecyclerView recyclerView;
    public static MyRecyclerAdapter adapter;
    public static LinearLayout liner1;
    private String userID;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.mainTool);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate: hello from Main activity");

        //create recycler view
        recyclerView = findViewById(R.id.myRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        liner1 = findViewById(R.id.liner1);




        //recycler view
        adapter = new MyRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        //work with database
        //declare the database reference object. This is what we use to access the database.
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        myReference = database.getReference("Users").child(userID);



        readFromFirebase();

        /*mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MyAssignment item = dataSnapshot.getValue(MyAssignment.class);

                new MyTask(getApplicationContext()).execute();

                if(item != null) {
                    addAssigments(item);
                }
                checkingVisibility();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialogFragment.show(getSupportFragmentManager(), "dialog fragment");
                callActivity(AddTaskActivity.class);

            }
        });





    }

    private void readFromFirebase() {
        Query myQuery = myReference.child("Assignments");
        myQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MyAssignment item = dataSnapshot.getValue(MyAssignment.class);

                new MyTask(getApplicationContext()).execute();

                if(item != null) {
                    addAssigments(item);
                }
                checkingVisibility();

                Log.d(TAG, "onChildReaded: " + item.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.singout:
               finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: MainActivity");
        checkingVisibility();

    }

    //add new item to recycler view
    public static void addAssigments(MyAssignment ass) {
        adapter.add(ass);

    }


    public static void checkingVisibility() {
        if (!adapter.myList.isEmpty()) {
            liner1.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);
            liner1.setLayoutParams(params);
            Log.d(TAG, "checkingVisibility: ");
        }
    }

    private void callActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


}
