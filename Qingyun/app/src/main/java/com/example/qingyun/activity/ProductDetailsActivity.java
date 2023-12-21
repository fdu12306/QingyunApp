package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qingyun.R;
import com.example.qingyun.adapter.ProductCommentAdapter;
import com.example.qingyun.bean.Comment;
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
    private EditText editTextComment;
    private Button buttonSubmitComment;
    private RecyclerView recyclerViewComments;
    private Integer id;
    private List<Comment> commentList;
    private static final String collectUrl  = AppConfig.BaseUrl+"/collect.php";
    private static final String orderUrl  = AppConfig.BaseUrl+"/order.php";
    private static final String issueCommentUrl=AppConfig.BaseUrl+"/issueComment.php";
    private static final String getCommentUrl=AppConfig.BaseUrl+"/getComment.php";
    private static final String addVisitRecorderUrl=AppConfig.BaseUrl+"/addVisitRecorder.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        initListener();
        int productId = getIntent().getIntExtra("productId", -1);
        this.id=productId;
        fetchProductDetails(productId);
        loadCommentData();
        addVisitRecorder();
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
        editTextComment=findViewById(R.id.editTextComment);
        buttonSubmitComment=findViewById(R.id.buttonSubmitComment);
        recyclerViewComments=findViewById(R.id.recyclerViewComments);
    }

    private void initListener(){
        buttonAddToWishlist.setOnClickListener(this);
        buttonBuyNow.setOnClickListener(this);
        buttonSubmitComment.setOnClickListener(this);
    }

    private void addVisitRecorder(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        RequestParams requestParams = new RequestParams();
        requestParams.put("productId", id);
        asyncHttpClient.post(addVisitRecorderUrl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // 处理失败的情况
                Toast.makeText(ProductDetailsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
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
        } else if(viewId==R.id.buttonSubmitComment){//发布评论
            issueComment();
        }
    }

    private void issueComment(){
        String comment=editTextComment.getText().toString().trim();
        if(comment.isEmpty()){
            Toast.makeText(ProductDetailsActivity.this, "您输入的评论为空", Toast.LENGTH_SHORT).show();
        }
        else{
            RequestParams requestParams = new RequestParams();
            requestParams.put("productId",this.id);
            requestParams.put("content",comment);
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            String myCookie = loadCookieFromSharedPreferences();
            asyncHttpClient.addHeader("Cookie", myCookie);
            asyncHttpClient.post(issueCommentUrl, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // 将 byte 数组转换为字符串
                    String responseString = new String(responseBody);
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

    private void loadCommentData(){
        commentList=new ArrayList<>();
        RequestParams requestParams = new RequestParams();
        requestParams.put("productId",this.id);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.post(getCommentUrl, requestParams, new AsyncHttpResponseHandler() {
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
                            String username=item.getString("username");
                            String content=item.getString("content");
                            String issueTime=item.getString("issueTime");
                            int deleteState=item.getInt("deleteState");
                            int isOwner=item.getInt("isOwner");
                            Comment comment=new Comment(id,username,content,issueTime,deleteState,isOwner);
                            commentList.add(comment);
                        }
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        ProductCommentAdapter productCommentAdapter = new ProductCommentAdapter(commentList,ProductDetailsActivity.this);
                        // Set up GridLayoutManager with spanCount 1
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductDetailsActivity.this, 1);
                        int verticalSpacingInPixels = 10;
                        recyclerViewComments.addItemDecoration(new VerticalSpaceItemDecoration(verticalSpacingInPixels));
                        recyclerViewComments.setLayoutManager(gridLayoutManager);
                        recyclerViewComments.setAdapter(productCommentAdapter);
                    }
                    else{
                        String message = jsonResponse.getString("message");
                        // 在 Toast 中显示成功消息
                        Toast.makeText(ProductDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
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