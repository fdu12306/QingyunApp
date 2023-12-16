package com.example.qingyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qingyun.R;
import com.example.qingyun.activity.ProductDetailsActivity;
import com.example.qingyun.bean.Product;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.ViewHolder>{
    private List<Product> productList;
    private Context context;
    private static final String deleteSoldUrl= AppConfig.BaseUrl+"/deleteSold.php";
    public SoldAdapter(List<Product> productList, Context context) {
        this.productList=productList;
        this.context = context;
    }
    @NonNull
    @Override
    public SoldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sold, parent, false);
        return new SoldAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        // 设置商品信息
        holder.productNameTextView.setText("商品名称："+product.getProductName());
        holder.productPriceTextView.setText("商品价格：¥" + product.getPrice());
        holder.soldTimeTextView.setText("出售时间："+product.getSoldTime());

        // 加载商品图片
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

        // 删除记录
        Button deleteButton = holder.itemView.findViewById(R.id.buttonDeleteRecord);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                RequestParams requestParams = new RequestParams();
                requestParams.put("productId",product.getId());
                asyncHttpClient.post(deleteSoldUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 将 byte 数组转换为字符串
                        String responseString = new String(responseBody);
                        try{
                            JSONObject jsonResponse = new JSONObject(responseString);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(context, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
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
        TextView soldTimeTextView;
        Button buttonDeleteRecord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewProductPrice);
            soldTimeTextView=itemView.findViewById(R.id.textViewSoldTime);
            buttonDeleteRecord=itemView.findViewById(R.id.buttonDeleteRecord);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}
