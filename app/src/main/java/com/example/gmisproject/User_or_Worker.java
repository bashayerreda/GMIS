package com.example.gmisproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class User_or_Worker extends AppCompatActivity {

    Button buttonUser, buttonWorker;
    Task<Void> databaseReference;
    String email, username, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or__worker);

        buttonUser = findViewById(R.id.btn_user);
        buttonWorker = findViewById(R.id.btn_worker);

        username = getIntent().getExtras().getString("username");
        email = getIntent().getExtras().getString("email");

        buttonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tips for guide new users
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        Intent intentTips = new Intent(User_or_Worker.this, tips.class);
                        startActivity(intentTips);

                    }
                };
                Handler handler = new Handler();
                handler.postDelayed(runnable, 50);

                UsersModel user = new UsersModel(email, username, type = "عميل");
                if (FirebaseAuth.getInstance().getUid() != null) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // set tokenId for each user
                                        getToken();
                                      Toast.makeText(User_or_Worker.this, "User created...", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(User_or_Worker.this, "error.....", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
        });


        buttonWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UsersModel user = new UsersModel(email, username, type = "عامل");
                if (FirebaseAuth.getInstance().getUid() != null) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // set tokenId for each employee
                                        getToken();
                                        Toast.makeText(User_or_Worker.this, "Employee created...", Toast.LENGTH_SHORT).show();
                                        Intent intentWorker = new Intent(User_or_Worker.this, Employee.class);
                                        startActivity(intentWorker);
                                    } else {
                                        Toast.makeText(User_or_Worker.this, "error.....", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    // method to get tokenId
    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("tokenId")
                                .setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                } else {
                                    Toast.makeText(User_or_Worker.this, "error.....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

    }
}

