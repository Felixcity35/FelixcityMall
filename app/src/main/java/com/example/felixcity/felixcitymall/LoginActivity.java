package com.example.felixcity.felixcitymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felixcity.felixcitymall.Model.Users;
import com.example.felixcity.felixcitymall.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputPassword,inputPhoneNumber;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton =findViewById(R.id.login_btn);
        inputPhoneNumber = findViewById(R.id.login_phone_number_input);
        inputPassword =findViewById(R.id.login_password);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_ckbn);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });

    }

    private void loginUser() {

        String phone =inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

          if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please Write Your Phone Number...",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Write Your Password...",Toast.LENGTH_LONG).show();
        }
        else{

              loadingBar.setTitle("Login Account");
              loadingBar.setMessage("Please Wait, While we are checking the Credentials. ");
              loadingBar.setCanceledOnTouchOutside(false);
              loadingBar.show();

              AllowAccessToAccount(phone,password);
          }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

                 if(chkBoxRememberMe.isChecked())
                 {
                     Paper.book().write(Prevalent.UserPhoneKey,phone);
                     Paper.book().write(Prevalent.UserPasswordKey,password);
                 }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users userData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    assert userData != null;
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                           if(parentDbName.equals("Admins"))
                           {
                               Toast.makeText(LoginActivity.this,"Welcome Admin,You have Logged in Successfully..",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this,AdminCategoryActivity.class);
                               startActivity(intent);
                           }
                           else if(parentDbName.equals("Users"))
                           {
                               Toast.makeText(LoginActivity.this,"Login Successfull..",Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                               startActivity(intent);
                           }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Password is incorrect..",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                 Toast.makeText(LoginActivity.this,"Acount with this "+ phone +" Already exist",Toast.LENGTH_LONG).show();
                 loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
