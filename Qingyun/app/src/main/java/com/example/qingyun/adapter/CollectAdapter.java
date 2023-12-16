package com.example.qingyun.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>{
    private List<Product> productList;
    private Context context;
    private static final String removeCollectUrl= AppConfig.BaseUrl+"/removeCollect.php";
    private static final String setPriceThresholdUrl=AppConfig.BaseUrl+"/setPriceThreshold.php";

    public CollectAdapter(List<Product> productList, Context context) {
        this.productList=productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // 设置商品信息
        holder.productNameTextView.setText("商品名称："+product.getProductName());
        holder.productPriceTextView.setText("商品价格：¥" + product.getPrice());
        holder.productPriceThresholdTextView.setText("期望价格：¥" + product.getPriceThreshold());

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

        // 移出收藏按钮点击事件
        Button removeButton = holder.itemView.findViewById(R.id.buttonRemove);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                RequestParams requestParams = new RequestParams();
                requestParams.put("productId",product.getId());
                asyncHttpClient.post(removeCollectUrl, requestParams, new AsyncHttpResponseHandler() {
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

        // 设置价格下限按钮点击事件
        Button setMinPriceButton = holder.itemView.findViewById(R.id.buttonPriceThreshold);
        setMinPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建 AlertDialog.Builder 对象
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("设置期望价格");
                // 使用 LayoutInflater 加载自定义的布局文件
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_set_price, null);
                builder.setView(dialogView);
                // 获取布局文件中的控件
                EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);

                // 设置确定按钮点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 获取用户输入的价格
                        String inputPrice = editTextPrice.getText().toString().trim();
                        if(inputPrice.isEmpty()){
                            Toast.makeText(context, "您输入的期望价格为空", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                            String myCookie = loadCookieFromSharedPreferences();
                            asyncHttpClient.addHeader("Cookie", myCookie);
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("productId",product.getId());
                            requestParams.put("priceThreshold",inputPrice);
                            asyncHttpClient.post(setPriceThresholdUrl, requestParams, new AsyncHttpResponseHandler() {
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
                        // 关闭对话框
                        dialogInterface.dismiss();
                    }
                });

                // 设置取消按钮点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 关闭对话框
                        dialogInterface.dismiss();
                    }
                });

                // 创建并显示对话框
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        TextView productPriceThresholdTextView;
        Button buttonRemove;
        Button buttonPriceThreshold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            productPriceTextView = itemView.findViewById(R.id.textViewProductPrice);
            productPriceThresholdTextView = itemView.findViewById(R.id.textViewProductPriceThreshold);
            buttonRemove=itemView.findViewById(R.id.buttonRemove);
            buttonPriceThreshold=itemView.findViewById(R.id.buttonPriceThreshold);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}
