package com.example.gmisproject;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class explicit_star_rating_activity extends AppCompatActivity {
    String username;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starforrating_activity);
        //prevent ScreenRotation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //views
        ImageView imageViewBackFromRatingPage = findViewById(R.id.image_view_backfrom_ratingpage);
        Button  buttonSendCompliantSFromUser = findViewById(R.id.button_send_complaints);
        final RatingBar RatingFromUser = findViewById(R.id.explicit_Rating_bar_id);
        //Retrieve data from firebase to save last userRate whatever happened
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("complaintmessages") .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rate");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //check method
                if(dataSnapshot != null && dataSnapshot.getValue() != null){
                    int Rate =  Integer.parseInt(dataSnapshot.getValue().toString());
                    RatingFromUser.setRating(Rate);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        imageViewBackFromRatingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(explicit_star_rating_activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        RatingFromUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //cast rate from float to integer
                int RatingResult = (int) Math.round(rating);
                // save it in string
                String rate = String.valueOf(RatingResult);
                //get Email
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                //get Username
                final DatabaseReference myRef = database.getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("username");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        username =  dataSnapshot.getValue().toString();
                        FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(username );
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                });

                //get current firebaseUser
                String userFirebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                //write data in firebase

                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email );
                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rate").setValue(rate );
                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("id").setValue(userFirebase);


            }
        });
        buttonSendCompliantSFromUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userFirebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // if (firebaseAuth.getCurrentUser() != null)
                //get email
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                //get username
                final DatabaseReference myRef = database.getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("username");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //check method
                        //String username = dataSnapshot.getValue(UsersModel.class);

                        username =  dataSnapshot.getValue().toString();
                        FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("username").setValue(username );

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value

                    }
                });
                final EditText editTextInputComplaintsUser = findViewById(R.id.edittext_view_addnotes);
                //report complaints for user
                String report = editTextInputComplaintsUser.getText().toString();
                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").setValue(email );
                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("report").push().setValue(report );
                FirebaseDatabase.getInstance().getReference("complaintmessages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("id").setValue(userFirebase);
                Toast.makeText(explicit_star_rating_activity.this,"تم الارسال",Toast.LENGTH_LONG).show();
                editTextInputComplaintsUser.setText("");

            }
        });
    }
}


