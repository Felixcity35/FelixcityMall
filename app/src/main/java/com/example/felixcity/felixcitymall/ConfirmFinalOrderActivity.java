package com.example.felixcity.felixcitymall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.felixcity.felixcitymall.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText nameEditText,phoneEditText,addressEditText,cityEditText  ;
    private Button confirmOrderbtn;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = N "+ totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderbtn = findViewById(R.id.confirm_final_order_btn);
        nameEditText = findViewById(R.id.shippment_name);
        phoneEditText = findViewById(R.id.shippment_phone_number);
        addressEditText = findViewById(R.id.shippment_address);
        cityEditText = findViewById(R.id.shippment_city);

        confirmOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void Check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide Your Full Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
          {
             Toast.makeText(this, "Please Provide Your Phone Number", Toast.LENGTH_SHORT).show();
          }
          else if(TextUtils.isEmpty(addressEditText.getText().toString()))
            {
            Toast.makeText(this, "Please Provide Your Address", Toast.LENGTH_SHORT).show();
            }
           else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Provide Your City Name", Toast.LENGTH_SHORT).show();
        }

        else
        {
            confirmOrder();
        }
    }

           private void confirmOrder()
           {
               final String saveCurrenDAte,saveCurrentTime;

               Calendar calForDate = Calendar.getInstance();
               SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
               saveCurrenDAte = currentDate.format(calForDate.getTime());

               SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
               saveCurrentTime = currentTime.format(calForDate.getTime());

               final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                       .child("Orders")
                       .child(Prevalent.currentOnlineUser.getPhone());

               HashMap<String,Object> ordersMap = new HashMap<>();

               ordersMap.put("TotalAmount", totalAmount);
               ordersMap.put("name", nameEditText.getText().toString());
               ordersMap.put("phone", phoneEditText.getText().toString());
               ordersMap.put("address", addressEditText.getText().toString());
               ordersMap.put("city", cityEditText.getText().toString());
               ordersMap.put("date", saveCurrenDAte);
               ordersMap.put("time", saveCurrentTime);
               ordersMap.put("state","not shipped");

               ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       FirebaseDatabase.getInstance().getReference().child("Cart List")
                               .child("User View")
                               .child(Prevalent.currentOnlineUser.getPhone())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                   }
               });



           }
}
