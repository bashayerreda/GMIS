package com.example.gmisproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;

public class Registration extends AppCompatActivity {
    static final int GOOGLE_SIGN_IN = 123;
    private static final String TAG = "GoogleActivity";
    private static final String TAG2 = "FacebookActivity";
    CallbackManager mCallbackManager;
    Button buttonSignInView, buttonSignUp, buttonSignIn;
    TextInputLayout editTextEmail, editTextPassword;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ImageView facebookLogin, googleLogin;
    BottomSheetDialog bottomSheetDialog;
    GoogleSignInClient mGoogleSignInClient;
    String type;
    ProgressBar progressBarAnimation;
    BottomSheetBehavior mBehavior;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        // VIEWS
        buttonSignInView = findViewById(R.id.btn_sign_in_bottom);

        //get instance from firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //  startActivity(new Intent(Registration.this, MainActivity.class));
                    checkUserType();
                }
            }
        };
        configGoogleSignIn();

        buttonSignInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showButtonSheet();
            }
        });
    }

    private void showButtonSheet() {
        // initiate BottomSheet Dialog
        bottomSheetDialog = new BottomSheetDialog(Registration.this);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                RelativeLayout bottomSheet = bottomSheetDialog.findViewById(R.id.bottom_sheet_layout);
                assert bottomSheet != null;
                ViewGroup.LayoutParams params = bottomSheet.getLayoutParams();
                bottomSheet.setLayoutParams(params);
            }
        });
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.bottomsheet_login, null);
        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();

        //VIEWS
        editTextEmail = bottomSheetDialog.findViewById(R.id.inputLayout_email);
        editTextPassword = bottomSheetDialog.findViewById(R.id.inputLayout_password);
        buttonSignIn = bottomSheetDialog.findViewById(R.id.btn_sign_in);
        buttonSignUp = bottomSheetDialog.findViewById(R.id.btn_signup);
        facebookLogin = bottomSheetDialog.findViewById(R.id.facebook_login);
        googleLogin = bottomSheetDialog.findViewById(R.id.gmail_login);
        progressBarAnimation = findViewById(R.id.spin_kit);

        //set custom font to inputLayout type password
        final Typeface typeface = ResourcesCompat.getFont(this, R.font.monadi);
        editTextPassword.setTypeface(typeface);

        //Go to SignUP Activity
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, SignUp.class);
                startActivity(intent);
            }

        });

        //Login button
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startSignIn();
            }

        });

        //Facebook Login
        mCallbackManager = CallbackManager.Factory.create();
        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAnimation.setVisibility(View.VISIBLE);
                facebookLogin(mCallbackManager);
            }
        });

        //Google Login
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                //implement progressBarAnimation make it visible
                progressBarAnimation.setVisibility(View.VISIBLE);
                bottomSheetDialog.dismiss();
                SignInGoogle();
            }
        });
    }

    private void facebookLogin(CallbackManager mCallbackManager) {
        progressBarAnimation.setVisibility(View.VISIBLE);
        LoginManager.getInstance().logInWithReadPermissions(Registration.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Log.d(TAG2, "facebook:onCancel");
                progressBarAnimation.setVisibility(View.GONE);

            }

            @Override
            public void onError(FacebookException error) {

                Log.d(TAG2, "facebook:onError", error);
                progressBarAnimation.setVisibility(View.GONE);
            }
        });
    }

    private void startSignIn() {
        final String email = editTextEmail.getEditText().getText().toString().trim();
        String password = editTextPassword.getEditText().getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Enter your email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Enter your password");
            editTextPassword.requestFocus();
            return;
        }
        //implement progressBarAnimation make it visible

        progressBarAnimation.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Remove load from memory
                    progressBarAnimation.setVisibility(View.GONE);
                    //     checkUserType();
                } else {
                    // Remove load from memory
                    progressBarAnimation.setVisibility(View.GONE);
                    editTextEmail.getEditText().setText("");
                    editTextPassword.getEditText().setText("");
                }
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

    public void checkUserType() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  assert type != null;
                type = dataSnapshot.getValue(String.class);
                if (type != null) {
                    if (type.equals("عميل")) {
                        // set tokenId for each user login
                        getToken();
                        startActivity(new Intent(Registration.this, MainActivity.class));
                        finish();
                    } else if (type.equals("عامل")) {
                        // set tokenId for each employee login
                        getToken();
                        startActivity(new Intent(Registration.this, Employee.class));
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void SignInGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == GOOGLE_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
            } else {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //Google Authentication
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // make it gone remove load from memory
                    progressBarAnimation.setVisibility(View.GONE);
                    //     checkUserType();
                } else {
                    // make it gone remove load from memory
                    progressBarAnimation.setVisibility(View.GONE);
                }
            }
        });
    }

    // Facebook Authentication
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Make it gone
                            progressBarAnimation.setVisibility(View.GONE);
                            //            checkUserType();
                        } else {
                            // make it gone remove load from memory

                            progressBarAnimation.setVisibility(View.GONE);
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
                                    Toast.makeText(Registration.this, "error.....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });
    }

}
