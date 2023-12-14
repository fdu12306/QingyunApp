package com.example.qingyun.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qingyun.R;
import com.example.qingyun.activity.CategoryActivity;
import com.example.qingyun.activity.SearchActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private EditText editTextSearch;
    private ImageView btnSearch;
    private Button book;
    private Button study;
    private Button electronic;
    private Button cosmetic;
    private Button sports;
    private Button equipment;
    private Button medical;
    private Button food;
    private Button others;

    private RecyclerView recyclerViewProducts;
    private static List<Product> productList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        editTextSearch=view.findViewById(R.id.editTextSearch);
        btnSearch=view.findViewById(R.id.btnSearch);
        book=view.findViewById(R.id.book);
        study=view.findViewById(R.id.study);
        electronic=view.findViewById(R.id.electronic);
        cosmetic=view.findViewById(R.id.cosmetic);
        sports=view.findViewById(R.id.sports);
        equipment=view.findViewById(R.id.equipment);
        medical=view.findViewById(R.id.medical);
        food=view.findViewById(R.id.food);
        others=view.findViewById(R.id.others);
        recyclerViewProducts=view.findViewById(R.id.recyclerViewProducts);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
        loadProductData();
    }

    private void initListener() {
        btnSearch.setOnClickListener(this);
        book.setOnClickListener(this);
        study.setOnClickListener(this);
        electronic.setOnClickListener(this);
        cosmetic.setOnClickListener(this);
        sports.setOnClickListener(this);
        equipment.setOnClickListener(this);
        medical.setOnClickListener(this);
        food.setOnClickListener(this);
        others.setOnClickListener(this);
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }

    private void loadProductData(){
        productList=new ArrayList<>();
        String getProductUrl= AppConfig.BaseUrl+"/getNewestProducts.php";
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getProductUrl, requestParams, new AsyncHttpResponseHandler() {
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
                            product.setId(id);
                            product.setProductName(productName);
                            product.setPrice(price);
                            product.setImagePath(imagePath);
                            productList.add(product);
                        }
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        // Create the adapter and set it to recyclerViewProducts
                        ProductAdapter productAdapter = new ProductAdapter(productList,requireContext());

                        // Set up GridLayoutManager with spanCount 3
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                        recyclerViewProducts.setLayoutManager(gridLayoutManager);

                        recyclerViewProducts.setAdapter(productAdapter);
                    }
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

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if(viewId==R.id.btnSearch){//搜索
            String input=editTextSearch.getText().toString().trim();
            if(input.isEmpty()){
                Toast.makeText(getActivity(), "请输入您要搜索的关键词", Toast.LENGTH_SHORT).show();
            }else {
                startSearchActivity(input);
            }
        } else {
            // 处理类别按钮点击
            String category = getCategoryFromButtonId(viewId);
            startCategoryActivity(category);
        }

    }

    private String getCategoryFromButtonId(Integer viewId){
        String category = "";
        if (viewId==R.id.book) {//图书资料
            category="图书资料";
        }else if (viewId==R.id.study) {//学习办公
            category="学习办公";
        }else if (viewId==R.id.electronic) {//电子数码
            category="电子数码";
        }else if (viewId==R.id.cosmetic) {//美容妆造
            category="美容彩妆";
        } else if (viewId==R.id.sports) {//运动健身
            category="运动健身";
        } else if (viewId==R.id.equipment) {//装备器材
            category="装备器材";
        } else if (viewId==R.id.medical) {//医疗保健
            category="医疗保健";
        } else if (viewId==R.id.food) {//食品饮料
            category="食品饮料";
        } else if (viewId==R.id.others) {//其他
            category="其他";
        }
        return category;
    }

    private void startCategoryActivity(String category) {
        // 创建 Intent 对象，传递类别信息
        Intent intent = new Intent(requireContext(), CategoryActivity.class);
        intent.putExtra("category", category);
        // 启动 CategoryActivity
        startActivity(intent);
    }

    private void startSearchActivity(String input){
        // 创建 Intent 对象，传递搜索内容信息
        Intent intent = new Intent(requireContext(), SearchActivity.class);
        intent.putExtra("input", input);
        startActivity(intent);
    }

}