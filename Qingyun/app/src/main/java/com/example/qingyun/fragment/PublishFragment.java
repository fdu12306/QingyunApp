package com.example.qingyun.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishFragment extends Fragment implements View.OnClickListener{
    private EditText editTextName;
    private EditText editTextDescription;
    private ImageView imageViewSelected;
    private Button btnAddImage;
    private Spinner spinnerCampus;
    private Spinner spinnerCategory;
    private EditText editTextPrice;
    private Button btnPublish;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_publish, container, false);
        editTextName=view.findViewById(R.id.editTextName);
        editTextDescription=view.findViewById(R.id.editTextDescription);
        imageViewSelected=view.findViewById(R.id.imageViewSelected);
        btnAddImage=view.findViewById(R.id.btnAddImage);
        spinnerCampus=view.findViewById(R.id.spinnerCampus);
        spinnerCategory=view.findViewById(R.id.spinnerCategory);
        editTextPrice=view.findViewById(R.id.editTextPrice);
        btnPublish=view.findViewById(R.id.btnPublish);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        btnAddImage.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
    }

    private byte[] getImageBytesFromUri(Uri selectedImageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
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

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if(viewId==R.id.btnAddImage){//添加图片
            // 创建一个Intent来启动系统的图片选择器
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        } else if (viewId==R.id.btnPublish) {//发布
            String productName=editTextName.getText().toString().trim();
            String description=editTextDescription.getText().toString().trim();
            String selectedCampus = spinnerCampus.getSelectedItem().toString();
            String selectedCategory=spinnerCategory.getSelectedItem().toString();
            String price=editTextPrice.getText().toString().trim();
            if(productName.isEmpty()){
                Toast.makeText(getActivity(), "请添加商品名称", Toast.LENGTH_SHORT).show();
            }else if(description.isEmpty()){
                Toast.makeText(getActivity(), "请添加商品描述", Toast.LENGTH_SHORT).show();
            }
            else if (selectedImageUri == null) {
                // 提示用户选择一张图片
                Toast.makeText(getActivity(), "请上传一张商品图片", Toast.LENGTH_SHORT).show();
            }
            else if(selectedCampus.equals("请选择校区")){
                Toast.makeText(getActivity(), "请选择校区", Toast.LENGTH_SHORT).show();
            }
            else if(selectedCategory.equals("请选择商品类别")){
                Toast.makeText(getActivity(), "请选择商品类别", Toast.LENGTH_SHORT).show();
            } else if (price.isEmpty()) {
                Toast.makeText(getActivity(), "请输入您意向的价格", Toast.LENGTH_SHORT).show();
            }
            else {
                // 将selectedImageUri中的图片数据转换为字节数组
                byte[] imageBytes = getImageBytesFromUri(selectedImageUri);
                RequestParams requestParams = new RequestParams();
                requestParams.put("productName",productName);
                requestParams.put("description",description);
                requestParams.put("campus",selectedCampus);
                requestParams.put("category",selectedCategory);
                requestParams.put("price",price);
                // 添加图片数据到RequestParams
                requestParams.put("image", new ByteArrayInputStream(imageBytes), "image.jpg");
                String url  = AppConfig.BaseUrl+"/issue.php";
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
                            // 例如，如果后端返回了一个名为 "message" 的字段
                            String message = jsonResponse.getString("message");
                            // 在 Toast 中显示成功消息
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(getActivity(), "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }
    }
}