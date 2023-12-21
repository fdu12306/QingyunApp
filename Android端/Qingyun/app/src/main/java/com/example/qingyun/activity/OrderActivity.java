package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.qingyun.R;
import com.example.qingyun.adapter.OrderAdapter;
import com.example.qingyun.adapter.SoldAdapter;
import com.example.qingyun.bean.Product;
import com.example.qingyun.decoration.VerticalSpaceItemDecoration;
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

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerViewOrderProducts;

    private static final String getOrderUrl= AppConfig.BaseUrl+"/getOrder.php";
    private static List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        recyclerViewOrderProducts=findViewById(R.id.recyclerViewOrderProducts);
        loadOrderProduct();
    }

    private void loadOrderProduct(){
        productList=new ArrayList<>();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        asyncHttpClient.get(getOrderUrl, requestParams, new AsyncHttpResponseHandler() {
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
                            String soldTime = item.getString("orderTime");
                            String imagePath=item.getString("imagePath");
                            Product product=new Product(id,productName,price,imagePath,soldTime);
                            productList.add(product);
                        }
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        OrderAdapter orderAdapter = new OrderAdapter(productList,OrderActivity.this);
//                        // Set up GridLayoutManager with spanCount 1
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(OrderActivity.this, 1);
                        int verticalSpacingInPixels = 10;
                        recyclerViewOrderProducts.addItemDecoration(new VerticalSpaceItemDecoration(verticalSpacingInPixels));
                        recyclerViewOrderProducts.setLayoutManager(gridLayoutManager);
                        recyclerViewOrderProducts.setAdapter(orderAdapter);
                    }
                    else{
                        String message = jsonResponse.getString("message");
                        // 在 Toast 中显示成功消息
                        Toast.makeText(OrderActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 解析失败，可以在这里处理异常
                    Toast.makeText(OrderActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(OrderActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}