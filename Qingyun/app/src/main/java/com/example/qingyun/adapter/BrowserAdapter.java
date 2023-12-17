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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qingyun.R;
import com.example.qingyun.activity.ProductDetailsActivity;
import com.example.qingyun.bean.Browser;
import com.example.qingyun.bean.Product;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BrowserAdapter extends RecyclerView.Adapter<BrowserAdapter.ViewHolder>{
    private List<Browser> browserList;
    private Context context;
    private static final String deleteBrowserUrl= AppConfig.BaseUrl+"/deleteBrowser.php";
    public BrowserAdapter(List<Browser> browserList, Context context) {
        this.browserList=browserList;
        this.context = context;
    }
    @NonNull
    @Override
    public BrowserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_browser, parent, false);
        return new BrowserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowserAdapter.ViewHolder holder, int position) {
        Browser browser=browserList.get(position);

        // 设置商品信息
        holder.productNameTextView.setText("商品名称："+browser.getProductName());
        holder.productPriceTextView.setText("商品价格：¥" + browser.getPrice());
        holder.browserTimeTextView.setText("浏览时间："+browser.getVisitTime());

        // 加载商品图片
        String imageUrl = browser.getImagePath(); // Assuming you have a getImagePath() method in your Product class
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
                intent.putExtra("productId", browser.getProductId()); // Pass the product ID or other necessary information
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
                requestParams.put("id",browser.getId());
                asyncHttpClient.post(deleteBrowserUrl, requestParams, new AsyncHttpResponseHandler() {
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
        return browserList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView browserTimeTextView;
        Button buttonDeleteRecord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewProductPrice);
            browserTimeTextView=itemView.findViewById(R.id.textViewBrowserTime);
            buttonDeleteRecord=itemView.findViewById(R.id.buttonDeleteRecord);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}
