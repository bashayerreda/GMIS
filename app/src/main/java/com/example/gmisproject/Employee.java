package com.example.gmisproject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

public class Employee extends AppCompatActivity {
    DatabaseReference referenceBins, referenceBinData;
    RecyclerView recyclerView;
    ArrayList<Integer> BinsData;
    UsersModel usersModel;
    BinsModel binsModel;
    String final_location;
    ImageView signOut;
    TextView textViewUsername;
    View binAlertLayout;
    ArrayList<BinsModel> binsModels;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String profilePicture;
    CircleImageView imageViewProfilePicture;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private long backPressedTime;
    private BinsAdapter mAdapter;

    public Employee() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        textViewUsername = findViewById(R.id.textView_userName);
        imageViewProfilePicture = findViewById(R.id.user_logo);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //set image profile
        assert user != null;
        if (user.getPhotoUrl() != null) {
            profilePicture = user.getPhotoUrl().toString();
            profilePicture += "?type=large";
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            Uri profilePicture = account.getPhotoUrl();
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
        setContentView(R.layout.activity_employee);

        signOut = findViewById(R.id.sign_out);
        textViewUsername = findViewById(R.id.textView_userName);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(Employee.this, Registration.class));
                }
            }
        };

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show alertDialog for employee to signOut or dismiss action
                final Dialog dialog = new Dialog(Employee.this);
                dialog.setContentView(R.layout.alertdialogsignoutemp);
                dialog.setCancelable(false);
                dialog.show();
                TextView textViewSignOutYes = dialog.findViewById(R.id.text_view_yesfor_signout_emp);
                TextView textViewSignOutNo = dialog.findViewById(R.id.text_view_no_for_signout_emp);
                textViewSignOutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // delete tokenId when employee logout...
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

        binAlertLayout = findViewById(R.id.alert_bin_layout);
        binsModels = new ArrayList<BinsModel>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        getUserBins();
        getBinsData();

    }

    private void getUserBins() {
        //reading the Bin id that user have
        referenceBins = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        referenceBins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersModel = dataSnapshot.getValue(UsersModel.class);
                assert usersModel != null;
                BinsData = usersModel.getBins();
                if (BinsData == null) {
                    recyclerView.setVisibility(View.GONE);
                    binAlertLayout.setVisibility(View.VISIBLE);
                } else {
                    getBinsData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Employee.this, "something went wrong ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBinsData() {
        // searching for the data of bin that user have
        referenceBinData = FirebaseDatabase.getInstance().getReference().child("Bins");
        referenceBinData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binsModels.clear();
                for (DataSnapshot binSnapshot : dataSnapshot.getChildren()) {
                    binsModel = binSnapshot.getValue(BinsModel.class);
                    if (usersModel != null && usersModel.getBins() != null) {
                        BinsData = usersModel.getBins();
                        for (int i = 0; i < BinsData.size(); i++) {
                            assert binsModel != null;
                            int binID = binsModel.getBinId();
                            int userBinID = usersModel.getBins().get(i);
                            if (binID == userBinID) {
                                binsModels.add(binsModel);
                            }
                        }
                    }
                }
                mAdapter = new BinsAdapter(binsModels);
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new BinsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String position1 = String.valueOf(position + 1);
                        DatabaseReference reference_location = FirebaseDatabase.getInstance().getReference("Bins");
                        DatabaseReference reference_location1 = reference_location.child(position1);
                        DatabaseReference final_ref_location = reference_location1.child("location");

                        final_ref_location.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final_location = String.valueOf(dataSnapshot.getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Intent intent = new Intent();

                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("google.navigation:q=" + final_location));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            Toast backToast = Toast.makeText(getBaseContext(), "Please Press Again", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
