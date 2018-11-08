package com.avi.todo.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avi.todo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AuthenticationActivity";

    Button login, btnRegister;
    EditText editEmail, editPassword;
    ProgressBar progressBar;
    
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login = findViewById(R.id.btnLogIN);
        btnRegister = findViewById(R.id.btnRegister);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        progressBar = findViewById(R.id.progressBar);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();


        //put listener for buttons
        btnRegister.setOnClickListener(this);
        login.setOnClickListener(this);


    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void callActivity(Class activity){
        Intent intent = new Intent(this,activity);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        String userEmail = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        switch (view.getId()){
            case R.id.btnLogIN:
                singIn(userEmail,password);
                break;
            case R.id.btnRegister:
                createAccount(userEmail,password);

                break;
                
        }
    }


    private void createAccount(String userEmail,String password) {
        Log.d(TAG, "createAccount: "  +  userEmail);
        if (!validateForm()) return;

        progressBar.setVisibility(View.VISIBLE);

        // create user with email
        mAuth.createUserWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"User request is successful ",Toast.LENGTH_LONG).show();
                            callActivity(MainActivity.class);
                        }else {
                            Toast.makeText(getApplicationContext(),"Some error occurred ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void singIn(String userEmail, String password) {
        Log.d(TAG, "singIn: " + userEmail);
        if(!validateForm())return;

        progressBar.setVisibility(View.VISIBLE);

        // sing in user with email
        mAuth.signInWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                            callActivity(MainActivity.class);
                        }else{
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //checking user's input
    private boolean validateForm() {
        boolean valid = true;

        String userEmail = editEmail.getText().toString().trim();
        if(userEmail.isEmpty()){
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            editEmail.setError("Please enter valid email");
            editEmail.requestFocus();
            valid = false;
        }

        String password = editPassword.getText().toString().trim();
        if(password.isEmpty()){
            editPassword.setError("Password id required");
            editPassword.requestFocus();
            valid = false;
        }

        if(password.length()<6){
            editPassword.setError("Minimum of password should be 6");
            editPassword.requestFocus();
            valid = false;

        }
        return valid;
    }


}
