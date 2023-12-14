package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qingyun.R;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String getProductDetailsUrl = AppConfig.BaseUrl + "/getProductDetails.php";

    private ImageView imageViewProduct;
    private TextView textViewProductName;
    private TextView textViewCampus;
    // 添加其他需要展示的 TextView
    private TextView textViewCategory;
    private TextView textViewPrice;
    private TextView textViewDescription;
    private TextView textViewPublishTime;
    private TextView textViewSoldState;
    private Button buttonAddToWishlist;
    private Button buttonBuyNow;
    private Integer id;
    private static final String collectUrl  = AppConfig.BaseUrl+"/collect.php";
    private static final String orderUrl  = AppConfig.BaseUrl+"/order.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        initListener();
        int productId = getIntent().getIntExtra("productId", -1);
        this.id=productId;
        fetchProductDetails(productId);
    }

    private void initView(){
        // 初始化 UI 元素
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewCampus = findViewById(R.id.textViewCampus);
        textViewCategory = findViewById(R.id.textViewProductCategory);
        textViewPrice = findViewById(R.id.textViewProductPrice);
        textViewDescription = findViewById(R.id.textViewProductDescription);
        textViewPublishTime = findViewById(R.id.textViewProductPublishTime);
        textViewSoldState = findViewById(R.id.textViewProductStatus);
        buttonAddToWishlist=findViewById(R.id.buttonAddToWishlist);
        buttonBuyNow=findViewById(R.id.buttonBuyNow);
    }

    private void initListener(){
        buttonAddToWishlist.setOnClickListener(this);
        buttonBuyNow.setOnClickListener(this);
    }

    private void fetchProductDetails(int productId) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", productId);
        asyncHttpClient.post(getProductDetailsUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    if (jsonResponse.has("data")) {
                        JSONObject productData = jsonResponse.getJSONObject("data");
                        // Extract detailed product information from productData
                        String productName = productData.getString("productName");
                        double price = productData.getDouble("price");
                        String campus = productData.getString("campus");
                        String category = productData.getString("category");
                        String description = productData.getString("description");
                        String publishTime = productData.getString("issueTime");
                        String imagePath=productData.getString("imagePath");
                        int soldState = productData.getInt("soldState");
                        // Populate the UI with detailed product information
                        Glide.with(ProductDetailsActivity.this)
                                .load(imagePath)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(imageViewProduct);

                        textViewProductName.setText("商品名称："+productName);
                        textViewCampus.setText("校区：" + campus);
                        textViewCategory.setText("商品类别：" + category);
                        textViewPrice.setText("商品价格：¥" + price);
                        textViewDescription.setText("商品描述：" + description);
                        textViewPublishTime.setText("发布时间：" + publishTime);
                        textViewSoldState.setText("出售状态：" + (soldState == 1 ? "已售出" : "未售出"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProductDetailsActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // 处理失败的情况
                Toast.makeText(ProductDetailsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.buttonAddToWishlist){//收藏
            operate(collectUrl);
        } else if (viewId==R.id.buttonBuyNow) {//购买
            operate(orderUrl);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }

    private void operate(String url){
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",this.id);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                Log.d("order",responseString);
                try {
                    // 解析字符串为 JSON 对象
                    JSONObject jsonResponse = new JSONObject(responseString);
                    // 在此处处理 jsonResponse，根据后端返回的数据结构进行操作
                    String message = jsonResponse.getString("message");
                    // 在 Toast 中显示成功或失败消息
                    Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 解析失败，可以在这里处理异常
                    Toast.makeText(ProductDetailsActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ProductDetailsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}