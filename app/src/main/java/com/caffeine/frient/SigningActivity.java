package com.caffeine.frient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caffeine.frient.Activities.UIActivity;
import com.caffeine.frient.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigningActivity extends AppCompatActivity {

    private View loginView, signUpView;
    private TextView login, signup, nextTxt, forgotPass, logIn;
    private RelativeLayout loginBtn, signUpBtn;
    private Animation slide_right, slide_left, new_slide_left, new_slide_right;

    private int count = 0;
    private LinearLayout loginLayout, nameEmail, passPass, dateGender;
    private RelativeLayout signupLayout, prev, next;

    private TextInputEditText loginEmail, loginPass, name, email, pass, confirmPass;
    private TextView male, female;
    private EditText day, month, year;
    private String Name, Email, Pass, ConfirmPass, Gender = "", Day, Month, Year, DOB;
    public static final String EMAIL_PATTERN = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

    private Dialog popup, progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);

        getLayoutIDs();
        performAnimations();
        onClickListeners();
        dobJump();
        loginUser();
    }

    private void getLayoutIDs(){
        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_pass);
        forgotPass = findViewById(R.id.forgot_pass);
        logIn = findViewById(R.id.login);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        pass = findViewById(R.id.signup_pass);
        confirmPass = findViewById(R.id.signup_confirm_pass);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        day = findViewById(R.id.day);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);

        loginView = findViewById(R.id.login_view);
        signUpView = findViewById(R.id.sing_up_view);
        login = findViewById(R.id.login_txt);
        signup = findViewById(R.id.sign_up_txt);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);

        loginLayout = findViewById(R.id.login_layout);
        signupLayout = findViewById(R.id.sign_up_layout);
        nameEmail = findViewById(R.id.name_email);
        passPass = findViewById(R.id.pass_pass);
        dateGender = findViewById(R.id.date_gender);

        prev = findViewById(R.id.prev_btn);
        next = findViewById(R.id.next_btn);
        nextTxt = findViewById(R.id.next_txt);

        slide_right = AnimationUtils.loadAnimation(SigningActivity.this, R.anim.slide_right);
        slide_left = AnimationUtils.loadAnimation(SigningActivity.this, R.anim.slide_left);
        new_slide_left = AnimationUtils.loadAnimation(SigningActivity.this, R.anim.new_slide_left);
        new_slide_right = AnimationUtils.loadAnimation(SigningActivity.this, R.anim.new_slide_right);

        popup = new Dialog(this);
        progressBar = new Dialog(this);
    }

    private void initializeForSignup(){
        Name = name.getText().toString();
        Email = email.getText().toString();
        Pass = pass.getText().toString();
        ConfirmPass = confirmPass.getText().toString();
        Day = day.getText().toString();
        Month = month.getText().toString();
        Year = year.getText().toString();

        if (Day.length() < 2){
            Day = "0" + Day;
        }

        if (Month.length() < 2){
            Month = "0" + Month;
        }

        DOB = Day + "-" + Month + "-" + Year;
    }

    private void performAnimations(){
        next.setOnClickListener(view -> {
            switch (count){
                case 0:
                    nameEmail.startAnimation(new_slide_left);
                    passPass.setVisibility(View.VISIBLE);
                    passPass.startAnimation(slide_left);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count=1;
                            nameEmail.setVisibility(View.GONE);
                            prev.setVisibility(View.VISIBLE);
                        }
                    }, 250);

                    break;

                case 1:
                    passPass.startAnimation(new_slide_left);
                    dateGender.setVisibility(View.VISIBLE);
                    dateGender.startAnimation(slide_left);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            count=2;
                            passPass.setVisibility(View.GONE);
                            nextTxt.setText("Sign Up");
                        }
                    }, 250);

                    break;

                case 2:
                    initializeForSignup();
                    if (informationIsValid()){
                        showProgressBar();
                        signUpUser();
                    }

                    break;
            }
        });

        prev.setOnClickListener(view -> {
            switch (count){
                case 2:
                    dateGender.startAnimation(new_slide_right);
                    passPass.setVisibility(View.VISIBLE);
                    passPass.startAnimation(slide_right);
                    nextTxt.setText("Next");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dateGender.setVisibility(View.GONE);
                            count=1;
                        }
                    }, 250);

                    break;

                case 1:
                    passPass.startAnimation(new_slide_right);
                    nameEmail.setVisibility(View.VISIBLE);
                    nameEmail.startAnimation(slide_right);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passPass.setVisibility(View.GONE);
                            prev.setVisibility(View.INVISIBLE);
                            count=0;
                        }
                    }, 250);

                    break;
            }
        });
    }

    private void onClickListeners(){
        loginBtn.setOnClickListener(view -> {
            loginView.setVisibility(View.VISIBLE);
            signUpView.setVisibility(View.GONE);
            login.setTextColor(getResources().getColor(R.color.background));
            signup.setTextColor(getResources().getColor(R.color.colorWhite));

            signupLayout.startAnimation(new_slide_right);
            loginLayout.setVisibility(View.VISIBLE);
            loginLayout.startAnimation(slide_right);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    signupLayout.setVisibility(View.GONE);
                }
            }, 250);
        });

        signUpBtn.setOnClickListener(view -> {
            signUpView.setVisibility(View.VISIBLE);
            loginView.setVisibility(View.GONE);
            signup.setTextColor(getResources().getColor(R.color.background));
            login.setTextColor(getResources().getColor(R.color.colorWhite));

            loginLayout.startAnimation(new_slide_left);
            signupLayout.setVisibility(View.VISIBLE);
            signupLayout.startAnimation(slide_left);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginLayout.setVisibility(View.GONE);
                }
            }, 250);
        });

        male.setOnClickListener(view -> {
            female.setBackgroundResource(R.drawable.bg_white);
            male.setBackgroundResource(R.drawable.bg_yellow);
            Gender = "Male";
        });

        female.setOnClickListener(view -> {
            female.setBackgroundResource(R.drawable.bg_yellow);
            male.setBackgroundResource(R.drawable.bg_white);
            Gender = "Female";
        });
    }

    private boolean informationIsValid(){
        boolean v = true;

        if (Name.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't defined your name. Please enter your name to continue");
        }

        else if (Name.length() < 3){
            v = false;
            showPopupDialog("Your name is too short. Please enter your full name");
        }

        else if (Name.length() > 20){
            v = false;
            showPopupDialog("Your name is too long. Please make it a little bit smaller");
        }

        else if (Email.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't defined your email. Please enter your email to continue");
        }

        else if (!Email.matches(EMAIL_PATTERN)){
            v = false;
            showPopupDialog("Hey, it looks like you have entered an invalid email, Please make sure you entered a valid one");
        }

        else if (Pass.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't defined your password. Please enter a password to continue");
        }

        else if (Pass.length() < 8 || Pass.length() > 20){
            v = false;
            showPopupDialog("Hey, your password should be 8 to 20 characters long. Please fix it to continue");
        }

        else if (ConfirmPass.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't confirmed your password. Please confirm your password to continue");
        }

        else if (!Pass.equals(ConfirmPass)){
            v = false;
            showPopupDialog("Hey, the passwords didn't match. Make sure you entered correctly");
        }

        else if (Gender.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't defined your gender. Please define your gender to continue");
        }

        else if (Day.isEmpty() || Month.isEmpty() || Year.isEmpty()){
            v = false;
            showPopupDialog("Hey, it looks you haven't defined your date of birth. Please define your date of birth to continue");
        }

        else if (Integer.parseInt(Day) < 1 || Integer.parseInt(Day) > 31 || Integer.parseInt(Month) < 1 || Integer.parseInt(Month) > 12
                                            || Year.length() < 4){
            v = false;
            showPopupDialog("Hey, it looks you have entered an invalid date. Please make sure you entered it correctly");
        }

        return v;
    }

    private void showPopupDialog(String m){
        popup.setContentView(R.layout.popup_dialog);
        popup.setCancelable(false);

        TextView message, ok;
        message = popup.findViewById(R.id.popup_message);
        ok = popup.findViewById(R.id.action_ok);
        message.setText(m);

        ok.setOnClickListener(view -> {
            popup.dismiss();
        });

        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.show();
    }

    private void showProgressBar(){
        progressBar.setContentView(R.layout.progress_bar);
        progressBar.setCancelable(false);
        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.show();
    }

    private void dobJump(){
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (day.length() == 2){
                    month.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (month.length() == 2){
                    year.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void loginUser(){
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String LGEmail, LGPass;
                LGEmail = loginEmail.getText().toString();
                LGPass = loginPass.getText().toString();

                if (LGEmail.isEmpty()){
                    showPopupDialog("Hey, it looks you haven't defined your email. Please enter your email to continue");
                }

                else if (!LGEmail.matches(EMAIL_PATTERN)){
                    showPopupDialog("Hey, it looks like you have entered an invalid email, Please make sure you entered a valid one");
                }

                else if (LGPass.isEmpty()){
                    showPopupDialog("Hey, it looks you haven't defined your password. Please enter a password to continue");
                }

                else {
                    LoginUserWithEmailAndPassword(LGEmail, LGPass);
                    showProgressBar();
                }
            }
        });
    }

    private void LoginUserWithEmailAndPassword(String E, String P){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(E, P).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SigningActivity.this, UIActivity.class));
                    finish();
                    progressBar.dismiss();
                }

                else {
                    progressBar.dismiss();
                    showPopupDialog("The email or password you entered did not match with any account, please check it is correct or not");
                }
            }
        });
    }

    private void signUpUser(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendUserDataToFirebase();
                }

                else {
                    progressBar.dismiss();
                    showPopupDialog("An error occurred or the email you entered has been registered already, please try again later");
                }
            }
        });
    }

    private void sendUserDataToFirebase(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users");
        UserDetails user = new UserDetails(FirebaseAuth.getInstance().getUid(),
                                            Name, Email, Pass, Gender, DOB, Long.toString(System.currentTimeMillis()), "empty");

        ref.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SigningActivity.this, UIActivity.class));
                    finish();
                    progressBar.dismiss();
                }

                else {
                    progressBar.dismiss();
                    showPopupDialog("An error occurred while creating your account, please try again later");
                }
            }
        });
    }
}