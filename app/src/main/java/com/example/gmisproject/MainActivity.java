package com.example.gmisproject;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.gmisproject.user.UserFragmentAdapter;
import com.example.gmisproject.user.UserMsgModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView textViewUsername;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String profilePicture;
    CircleImageView imageViewProfilePicture;
    private long backPressedTime;
    private Toast backToast;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ImageView ImageViewStar;
    FloatingActionButton floatingButtonSignOut;
    UserMsgModel userMsgModel;
    String currentUserId,userId;
    private ArrayList<Integer> BinsData;
    private UsersModel usersModel;
    ViewPager viewPager;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        textViewUsername = findViewById(R.id.textView_userName);
        imageViewProfilePicture = findViewById(R.id.user_logo);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //set image profile
        if (user.getPhotoUrl() != null) {
            profilePicture = user.getPhotoUrl().toString();
            profilePicture += "?type=large";
            Picasso.get().load(profilePicture).fit().placeholder(R.drawable.user_logo).into(imageViewProfilePicture);
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userId);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                textViewUsername.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, Registration.class));
                }
            }
        };

        configGoogleSignIn();

        // method for sending notification message for specific user
        getUserMessageNotification();

        //Set id for Text view
        ImageViewStar = findViewById(R.id.image_view_star);
        ImageViewStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, explicit_star_rating_activity.class);
                startActivity(intent);
            }
        });
        floatingButtonSignOut = findViewById(R.id.floating_button_signout);
        floatingButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show alertDialog for user to signOut or dismiss action
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.alertdialogsignoutuser);
                dialog.setCancelable(false);
                dialog.show();
                TextView textViewSignOutYes = dialog.findViewById(R.id.text_view_yesfor_signout_user);
                TextView textViewSignOutNo = dialog.findViewById(R.id.text_view_no_for_signout_user);
                textViewSignOutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // delete tokenId when user logout...
                        databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("tokenId");
                        databaseReference.removeValue();
                        //sign out method
                        FirebaseAuth.getInstance().signOut();

                    }
                });
                textViewSignOutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });



        // Find the view pager that will allow the user to swipe between fragments
        viewPager = findViewById(R.id.viewpager);
        // Create an adapter that knows which fragment should be shown on each page
        UserFragmentAdapter adapter = new UserFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setPageTransformer(true, new ZoomAnimation());
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // set current item for viewPager depending on userBins
        checkUserBins();

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        //   viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.getTabAt(0).setIcon(R.drawable.bin);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_request);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_mail);


    }

    // send notification message for specific user

    public void getUserMessageNotification(){
        DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference("Responses");
        msgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userMsgModel = ds.getValue(UserMsgModel.class);
                    userId = userMsgModel.getId();
                    currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (userId.equals( currentUserId)) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("notify", "notify", NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager manager = getSystemService(NotificationManager.class);
                            manager.createNotificationChannel(channel);
                        }
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "notify")
                                .setContentText("Messages")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setAutoCancel(true)
                                .setContentText("message delivered");
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                        managerCompat.notify(999, builder.build());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    private void configGoogleSignIn() {
        // Configure Google Sign In
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void checkUserBins() {
        //reading the Bin id that user have
        DatabaseReference referenceBins = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        referenceBins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersModel = dataSnapshot.getValue(UsersModel.class);
                assert usersModel != null;
                BinsData = usersModel.getBins();
                if (BinsData == null) {
                    viewPager.setCurrentItem(1);
                } else {
                    viewPager.setCurrentItem(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "something went wrong ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // backButton to close app
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Please Press Again", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}