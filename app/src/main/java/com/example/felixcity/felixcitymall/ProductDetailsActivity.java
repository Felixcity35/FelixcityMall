package com.example.felixcity.felixcitymall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.felixcity.felixcitymall.Model.Products;
import com.example.felixcity.felixcitymall.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName,productDescription,productPrice;
    private Button addToCartBtn;
    private ElegantNumberButton numberButton;
    private String productId= "",state="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");

        addToCartBtn = findViewById(R.id.pd_add_to_cart_button);
        numberButton = findViewById(R.id.number_btn);
        productImage = findViewById(R.id.product_image_details);
        productName=findViewById(R.id.product_name_details);
        productDescription=findViewById(R.id.product_description_details);
        productPrice=findViewById(R.id.product_price_details);

       getProductDetailsId(productId);

       addToCartBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(state.equals("Order Placed") || state.equals("Order shipped"))
               {
                   Toast.makeText(ProductDetailsActivity.this, "you can add purchase more product,once your order is shipped or confirm", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   addingToCartList();
               }
           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime,saveCurrenDAte;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrenDAte = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

      final  DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

         final HashMap<String,Object> cartMaps = new HashMap<>();
         cartMaps.put("pid", productId);
        cartMaps.put("pname", productName.getText().toString());
        cartMaps.put("price", productPrice.getText().toString());
        cartMaps.put("date", saveCurrenDAte);
        cartMaps.put("time", saveCurrentTime);
        cartMaps.put("quantity", numberButton.getNumber());
        cartMaps.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productId)
                .updateChildren(cartMaps)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                       if(task.isSuccessful())
                       {
                           cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                   .child("Products").child(productId)
                                   .updateChildren(cartMaps)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) 
                                       {
                                          if(task.isSuccessful())
                                          {
                                              Toast.makeText(ProductDetailsActivity.this, " Added to Cart List", Toast.LENGTH_SHORT).show();
                                              Intent intent = new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                              startActivity(intent);
                                          }
                                       }
                                   });
                       }
                    }
                });
    }

    private void getProductDetailsId(String productId)
    {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                       state = "Order Shipped";
                    }
                    else if (shippingState.equals("not shipped"))
                    {

                          state = "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
