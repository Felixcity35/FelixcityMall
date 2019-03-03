package com.example.felixcity.felixcitymall.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.felixcity.felixcitymall.Interface.ItemClickListner;
import com.example.felixcity.felixcitymall.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProduct_Name,txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        txtProduct_Name = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
         this.listner = listner;
    }

    @Override
    public void onClick(View view) {

    }
}
