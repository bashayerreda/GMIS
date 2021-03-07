package com.example.gmisproject.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gmisproject.R;
import com.example.gmisproject.SignUp;
import com.example.gmisproject.UsersRequestsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserRequestFragment extends Fragment {
    DatabaseReference referenceRequest;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private View rootView;
    private String uId,payment;

    public UserRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_reqest, container, false);
        final TextInputLayout fullName = rootView.findViewById(R.id.textinput_use_fullName);
        final TextInputLayout Address = rootView.findViewById(R.id.textinput_user_address);
        final TextInputLayout binNumber = rootView.findViewById(R.id.textinput_user_binNumber);
        final TextInputLayout phoneNumber = rootView.findViewById(R.id.textinput_user_phone);
        radioGroup = rootView.findViewById(R.id.radio_group_cost_type);
        radioButton = rootView.findViewById(R.id.radio_btn_monthly);
        radioButton = rootView.findViewById(R.id.radio_btn_yearly);

        referenceRequest = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        uId=referenceRequest.getKey();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = radioGroup.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radio_btn_monthly:
                        payment = radioButton.getText().toString();
                        break;
                    case R.id.radio_btn_yearly:
                        payment = radioButton.getText().toString();
                        break;
                }
            }
        });


        final UsersRequestsModel usersRequestsModel = new UsersRequestsModel();

        Button btn = rootView.findViewById(R.id.button_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String fullname = fullName.getEditText().getText().toString();
                String address = Address.getEditText().getText().toString();
                String binNumbers = binNumber.getEditText().getText().toString();
                String phonenumber = phoneNumber.getEditText().getText().toString();

                if (fullname.isEmpty()) {
                    fullName.setError("برجاء ادخال الاسم");
                    fullName.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    Address.setError("برجاء ادخال العنوان");
                    Address.requestFocus();
                    return;
                }
                if(binNumbers.isEmpty()){
                    binNumber.setError("برجاء ادخال عدد السلات");
                    binNumber.requestFocus();
                    return;
                }
                if(phonenumber.isEmpty()){
                    phoneNumber.setError("برجاء ادخال عدد السلات");
                    phoneNumber.requestFocus();
                    return;
                }

                UsersRequestsModel usersRequestsModel = new UsersRequestsModel(uId,fullname, address, binNumbers, phonenumber, payment);

                FirebaseDatabase.getInstance().getReference("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(usersRequestsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "تم ارسال طلبك", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                fullName.getEditText().setText("");
                Address.getEditText().setText("");
                binNumber.getEditText().setText("");
                phoneNumber.getEditText().setText("");

            }
        });

        final RadioGroup radioGroup = rootView.findViewById(R.id.radio_group_cost_type);
        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = rootView.findViewById(radioId);
                //  Toast.makeText(UserRequestFragment.this.getActivity().getApplicationContext(), "نوع الحساب: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

}