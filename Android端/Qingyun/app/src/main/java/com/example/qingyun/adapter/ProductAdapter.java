package com.example.qingyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qingyun.R;
import com.example.qingyun.activity.ProductDetailsActivity;
import com.example.qingyun.bean.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    private Context context;

    public ProductAdapter(List<Product> productList,Context context) {
        this.productList = productList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set the product details to the ViewHolder
        holder.productNameTextView.setText(product.getProductName());
        holder.productPriceTextView.setText("¥" + product.getPrice());

        // Load the image for the specific product
        String imageUrl = product.getImagePath(); // Assuming you have a getImagePath() method in your Product class
        ImageView imageViewProduct = holder.itemView.findViewById(R.id.imageViewProduct);
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // 可以设置占位图
                .error(R.drawable.error_image) // 可以设置加载失败时显示的图片
                .into(imageViewProduct);

        //设置点击图片查看商品详情
        imageViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new Activity or Fragment to show product details
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productId", product.getId()); // Pass the product ID or other necessary information
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewProductPrice);
        }
    }
}
