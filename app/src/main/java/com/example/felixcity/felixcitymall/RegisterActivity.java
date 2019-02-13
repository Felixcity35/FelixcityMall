package com.example.felixcity.felixcitymall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText inputName,inputPhoneNumber,inputPassword ;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton =findViewById(R.id.register_btn);
        inputName = findViewById(R.id.register_username_input);
        inputPhoneNumber = findViewById(R.id.register_phone_number_input);
        inputPassword =findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name =inputName.getText().toString();
        String phone =inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Write Your Name...",Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please Write Your Phone Number...",Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Write Your Password...",Toast.LENGTH_LONG).show();
        }
        else{

              loadingBar.setTitle("Create Account");
              loadingBar.setMessage("Please Wait, While we are checking the Credentials. ");
              loadingBar.setCanceledOnTouchOutside(false);
              loadingBar.show();

              ValidatePhoneNumber(name,phone,password);
        }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
           final DatabaseReference RootRef;
             RootRef = FirebaseDatabase.getInstance().getReference();
             RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(!(dataSnapshot.child("Users").child(phone).exists()))
                     {
                         HashMap<String,Object> userDataMap =new  HashMap<>();
                          userDataMap.put("Phone",phone);
                          userDataMap.put("Password",password);
                          userDataMap.put("Name",name);

                          RootRef.child("Users").child(phone).updateChildren(userDataMap)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful()){
                                             Toast.makeText(RegisterActivity.this,"Congratulation Your Account has been Created",Toast.LENGTH_LONG).show();
                                             loadingBar.dismiss();
                                             Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                             startActivity(intent);
                                         }
                                         else{
                                             loadingBar.dismiss();
                                               Toast.makeText(RegisterActivity.this,"Network Error : Try Again Later...",Toast.LENGTH_LONG).show();
                                         }
                                      }
                                  });
                     }else

                     {
                         Toast.makeText(RegisterActivity.this,"This "+ phone + " already exits",Toast.LENGTH_LONG).show();
                         loadingBar.dismiss();
                         Toast.makeText(RegisterActivity.this,"Please try again using Another phone Number",Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                         startActivity(intent);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
    }
}
