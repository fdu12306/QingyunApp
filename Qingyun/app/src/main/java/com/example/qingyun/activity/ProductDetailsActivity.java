package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import cz.msebera.android.httpclient.Header;

public class ProductDetailsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        // 初始化 UI 元素
        imageViewProduct = findViewById(R.id.imageViewProduct);
        textViewProductName = findViewById(R.id.textViewProductName);
        textViewCampus = findViewById(R.id.textViewCampus);
        textViewCategory = findViewById(R.id.textViewProductCategory);
        textViewPrice = findViewById(R.id.textViewProductPrice);
        textViewDescription = findViewById(R.id.textViewProductDescription);
        textViewPublishTime = findViewById(R.id.textViewProductPublishTime);
        textViewSoldState = findViewById(R.id.textViewProductStatus);
        int productId = getIntent().getIntExtra("productId", -1);
        fetchProductDetails(productId);
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
                        Integer soldState = productData.getInt("soldState");
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

}