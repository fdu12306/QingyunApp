package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextSearch;
    private ImageView btnSearch;
    private TextView textViewSearchNum;
    private RecyclerView recyclerViewSearchProducts;
    private static List<Product> productList;
    private static final String searchUrl= AppConfig.BaseUrl+"/search.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initListener();
        // 获取传递的搜索内容信息
        Intent intent = getIntent();
        String input = intent.getStringExtra("input");
        editTextSearch.setText(input);
        loadSearchResult(input);
    }

    private void initView(){
        editTextSearch=findViewById(R.id.editTextSearch);
        btnSearch=findViewById(R.id.btnSearch);
        textViewSearchNum=findViewById(R.id.textViewSearchNum);
        recyclerViewSearchProducts=findViewById(R.id.recyclerViewSearchProducts);
    }

    private void initListener(){
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.btnSearch){
            String input=editTextSearch.getText().toString();
            if(input.isEmpty()){
                Toast.makeText(SearchActivity.this, "请输入您要搜索的关键词", Toast.LENGTH_SHORT).show();
            }
            else {
                loadSearchResult(input);
            }
        }
    }

    private void loadSearchResult(String input){
        productList=new ArrayList<>();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("input",input);
        asyncHttpClient.post(searchUrl, requestParams, new AsyncHttpResponseHandler() {
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
                        textViewSearchNum.setText("共查询到"+productList.size()+"条结果");
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        ProductAdapter productAdapter = new ProductAdapter(productList,SearchActivity.this);
                        // Set up GridLayoutManager with spanCount 3
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 3);
                        recyclerViewSearchProducts.setLayoutManager(gridLayoutManager);
                        recyclerViewSearchProducts.setAdapter(productAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // JSON 解析失败，可以在这里处理异常
                    Toast.makeText(SearchActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SearchActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}