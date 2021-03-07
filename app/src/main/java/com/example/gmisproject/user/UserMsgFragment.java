package com.example.gmisproject.user;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gmisproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.gmisproject.user.UserMsgModel.COMPLAINING_RESPONSE;
import static com.example.gmisproject.user.UserMsgModel.REQUEST_RESPONSE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserMsgFragment extends Fragment {
    UserMsgModel userMsgModel ;

    public UserMsgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_msg, container, false);


        final View alert_msg_layout = rootView.findViewById(R.id.alert_msg_layout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //object from userMsgModel
        userMsgModel = new UserMsgModel();
        //connection to firebase
        DatabaseReference myRef = database.getReference("Responses");
        //get currentUser
        final String currentUserFirebase = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final List<UserMsgModel> msgModelList = new ArrayList<UserMsgModel>();
        // msgModelList.add(new UserMsgModel(REQUEST_RESPONSE, getResources().getString(R.string.msg_response), "012245555", " 50 جنيه"));

        //Retrieve  list of data from firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again, whenever data at this location is updated
              // Remove unnecessary updates to each user
                msgModelList.clear();
                //loop data
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UserMsgModel userMsgModel = postSnapshot.getValue(UserMsgModel.class);
                    //check  method
                    if (userMsgModel.getId().equals(currentUserFirebase)){

                        msgModelList.add(userMsgModel);

                    }


                }
                UserMsgAdapter adapter = new UserMsgAdapter(msgModelList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if(msgModelList.isEmpty()){
                    alert_msg_layout.setVisibility(View.VISIBLE);
                }
                else alert_msg_layout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return rootView;
    }

}