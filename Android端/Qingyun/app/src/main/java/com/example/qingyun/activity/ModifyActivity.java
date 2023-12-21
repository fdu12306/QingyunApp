package com.example.qingyun.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextName;
    private EditText editTextDescription;
    private ImageView imageViewSelected;
    private Button btnAddImage;
    private Spinner spinnerCampus;
    private Spinner spinnerCategory;
    private EditText editTextPrice;
    private Button btnSubmit;
    private Integer id;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private static final String getProductDetailsUrl = AppConfig.BaseUrl + "/getProductDetails.php";
    private static final String modifyProductUrl=AppConfig.BaseUrl+"/modifyProduct.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
        initListener();
        int productId = getIntent().getIntExtra("productId", -1);
        this.id=productId;
        loadProductData(productId);
    }

    private void loadProductData(Integer productId){
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
                        String imagePath=productData.getString("imagePath");
                        // Populate the UI with detailed product information
                        Glide.with(ModifyActivity.this)
                                .load(imagePath)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .into(imageViewSelected);

                        editTextName.setText(productName);
                        editTextDescription.setText(description);
                        editTextPrice.setText(String.valueOf(price));

                        String[] campusOptions = getResources().getStringArray(R.array.campus_options);
                        int campusIndex= Arrays.asList(campusOptions).indexOf(campus);
                        spinnerCampus.setSelection(campusIndex);

                        String[] categoryOptions = getResources().getStringArray(R.array.category_options);
                        int categoryIndex= Arrays.asList(categoryOptions).indexOf(category);
                        spinnerCategory.setSelection(categoryIndex);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ModifyActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // 处理失败的情况
                Toast.makeText(ModifyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        editTextName=findViewById(R.id.editTextName);
        editTextDescription=findViewById(R.id.editTextDescription);
        imageViewSelected=findViewById(R.id.imageViewSelected);
        btnAddImage=findViewById(R.id.btnAddImage);
        spinnerCampus=findViewById(R.id.spinnerCampus);
        spinnerCategory=findViewById(R.id.spinnerCategory);
        editTextPrice=findViewById(R.id.editTextPrice);
        btnSubmit=findViewById(R.id.btnSubmit);
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            // 获取用户选择的图片的URI
                            selectedImageUri = result.getData().getData();
                            // 在ImageView中显示所选图片
                            imageViewSelected.setImageURI(selectedImageUri);
                        }
                    }
                });
    }

    private void initListener(){
        btnSubmit.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.btnSubmit){//提交修改
            String productName=editTextName.getText().toString().trim();
            String description=editTextDescription.getText().toString().trim();
            String selectedCampus = spinnerCampus.getSelectedItem().toString();
            String selectedCategory=spinnerCategory.getSelectedItem().toString();
            String price=editTextPrice.getText().toString().trim();
            if(productName.isEmpty()){
                Toast.makeText(ModifyActivity.this, "请添加商品名称", Toast.LENGTH_SHORT).show();
            }else if(description.isEmpty()){
                Toast.makeText(ModifyActivity.this, "请添加商品描述", Toast.LENGTH_SHORT).show();
            }
            else if(selectedCampus.equals("请选择校区")){
                Toast.makeText(ModifyActivity.this, "请选择校区", Toast.LENGTH_SHORT).show();
            }
            else if(selectedCategory.equals("请选择商品类别")){
                Toast.makeText(ModifyActivity.this, "请选择商品类别", Toast.LENGTH_SHORT).show();
            } else if (price.isEmpty()) {
                Toast.makeText(ModifyActivity.this, "请输入您意向的价格", Toast.LENGTH_SHORT).show();
            }
            else {
                RequestParams requestParams = new RequestParams();
                requestParams.put("id",this.id);
                requestParams.put("productName",productName);
                requestParams.put("description",description);
                requestParams.put("campus",selectedCampus);
                requestParams.put("category",selectedCategory);
                requestParams.put("price",price);
                if(selectedImageUri!=null){
                    // 将selectedImageUri中的图片数据转换为字节数组
                    byte[] imageBytes = getImageBytesFromUri(selectedImageUri);
                    // 添加图片数据到RequestParams
                    requestParams.put("image", new ByteArrayInputStream(imageBytes), "image.jpg");
                }
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                asyncHttpClient.post(modifyProductUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 将 byte 数组转换为字符串
                        String responseString = new String(responseBody);
                        try {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            String message = jsonResponse.getString("message");
                            // 在 Toast 中显示成功消息
                            Toast.makeText(ModifyActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(ModifyActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ModifyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if(viewId==R.id.btnAddImage){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }

    private byte[] getImageBytesFromUri(Uri selectedImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 处理异常情况
        }
    }


}