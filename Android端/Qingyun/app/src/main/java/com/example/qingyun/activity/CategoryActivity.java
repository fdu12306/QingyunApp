package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qingyun.R;
import com.example.qingyun.adapter.ProductAdapter;
import com.example.qingyun.bean.Product;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryActivity extends AppCompatActivity {
    private TextView textViewCategory;

    private RecyclerView recyclerViewCategoryProducts;

    private static final String getProductUrl= AppConfig.BaseUrl+"/getCategory.php";
    private static List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        // 获取传递的类别信息
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        textViewCategory.setText(category);
        loadCategoryProductData(category);
    }

    private void initView(){
        textViewCategory = findViewById(R.id.textViewCategory);
        recyclerViewCategoryProducts=findViewById(R.id.recyclerViewCategoryProducts);
    }

    private void loadCategoryProductData(String category) {
        productList=new ArrayList<>();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        requestParams.put("category",category);
        asyncHttpClient.post(getProductUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    if(jsonResponse.has("data")){
                        JSONArray dataArray=jsonResponse.getJSONArray("data");
                        for(int i=0;i<dataArray.length();i++){
                            JSONObject item=dataArray.getJSONObject(i);
                            int id=item.getInt("id");
                            String productName=item.getString("productName");
                            double price=item.getDouble("price");
                            String imagePath=item.getString("imagePath");
                            Product product=new Product(id,productName,price,imagePath);
                            productList.add(product);
                        }
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        ProductAdapter productAdapter = new ProductAdapter(productList,CategoryActivity.this);
                        // Set up GridLayoutManager with spanCount 3
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 3);
                        recyclerViewCategoryProducts.setLayoutManager(gridLayoutManager);
                        recyclerViewCategoryProducts.setAdapter(productAdapter);
                    }
                    else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 解析失败，可以在这里处理异常
                    Toast.makeText(CategoryActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(CategoryActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}