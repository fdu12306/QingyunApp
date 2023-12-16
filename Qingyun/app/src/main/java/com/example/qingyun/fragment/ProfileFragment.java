package com.example.qingyun.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qingyun.R;
import com.example.qingyun.activity.CollectActivity;
import com.example.qingyun.activity.LoginActivity;
import com.example.qingyun.activity.OrderActivity;
import com.example.qingyun.activity.PublishActivity;
import com.example.qingyun.activity.RegisterActivity;
import com.example.qingyun.activity.SoldActivity;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    private ImageView avatar;
    private TextView welcome;
    private Button loginBtn;
    private TextView div;
    private Button registerBtn;
    private Button collect;
    private Button browser;
    private Button comment;
    private Button issue;
    private Button sold;
    private Button order;
    private Button exit;
    private static final String checkLoginUrl= AppConfig.BaseUrl+"/checkLogin.php";
    private static final String logoutUrl=AppConfig.BaseUrl+"/logout.php";
    private static final String getCollectNumUrl=AppConfig.BaseUrl+"/getCollectNum.php";
    private static final String getIssueNumUrl=AppConfig.BaseUrl+"/getIssueNum.php";
    private static final String getSoldNumUrl=AppConfig.BaseUrl+"/getSoldNum.php";
    private static final String getOrderNumUrl=AppConfig.BaseUrl+"/getOrderNum.php";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        avatar=view.findViewById(R.id.avatar);
        welcome=view.findViewById(R.id.welcome);
        loginBtn=view.findViewById(R.id.login);
        div=view.findViewById(R.id.div);
        registerBtn=view.findViewById(R.id.register);
        collect=view.findViewById(R.id.collect);
        browser=view.findViewById(R.id.browser);
        comment=view.findViewById(R.id.comment);
        issue=view.findViewById(R.id.issue);
        sold=view.findViewById(R.id.sold);
        order=view.findViewById(R.id.order);
        exit=view.findViewById(R.id.exit);
        initLogDisplay();
        initCollectDisplay();
        initIssueDisplay();
        initSoldDisplay();
        initOrderDisplay();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        avatar.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        collect.setOnClickListener(this);
        browser.setOnClickListener(this);
        comment.setOnClickListener(this);
        issue.setOnClickListener(this);
        sold.setOnClickListener(this);
        order.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void initLogDisplay(){

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        // 在后续的请求中，从 SharedPreferences 中读取 Cookie，并将其添加到请求头中
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(checkLoginUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    Boolean logged_in=jsonResponse.getBoolean("logged");
                    if(logged_in) {
                        String username=jsonResponse.getString("username");
                        welcome.setText("欢迎你，"+username+"!");
                        welcome.setVisibility(View.VISIBLE);
                        loginBtn.setVisibility(View.GONE);
                        div.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.GONE);
                        exit.setVisibility(View.VISIBLE);
                    }else {
                        welcome.setVisibility(View.GONE);
                        loginBtn.setVisibility(View.VISIBLE);
                        div.setVisibility(View.VISIBLE);
                        registerBtn.setVisibility(View.VISIBLE);
                        exit.setVisibility(View.GONE);
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

    private void initCollectDisplay(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getCollectNumUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    int num=jsonResponse.getInt("data");
                    collect.setText(num+"\n收藏");
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

    private void initIssueDisplay(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getIssueNumUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    int num=jsonResponse.getInt("data");
                    issue.setText(num+"\n我发布的");
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

    private void initSoldDisplay(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getSoldNumUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    int num=jsonResponse.getInt("data");
                    sold.setText(num+"\n我卖出的");
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
    private void initOrderDisplay(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getOrderNumUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    int num=jsonResponse.getInt("data");
                    order.setText(num+"\n我购买的");
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
       if(viewId==R.id.avatar){//更换头像

       } else if (viewId==R.id.login) {//登录
           Intent intent = new Intent(getActivity(), LoginActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.register) {//注册
           Intent intent = new Intent(getActivity(), RegisterActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.collect) {//收藏
           Intent intent = new Intent(requireContext(), CollectActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.browser) {//浏览

       } else if (viewId==R.id.comment) {//评论

       } else if (viewId==R.id.issue) {//发布
           Intent intent = new Intent(requireContext(), PublishActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.sold) {//已卖出
           Intent intent = new Intent(requireContext(), SoldActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.order) {//已下单
           Intent intent = new Intent(requireContext(), OrderActivity.class);
           startActivity(intent);
       } else if (viewId==R.id.exit) {//退出登录
           AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
           RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
           String myCookie = loadCookieFromSharedPreferences();
           asyncHttpClient.addHeader("Cookie", myCookie);
           asyncHttpClient.get(logoutUrl, requestParams, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                   // 将 byte 数组转换为字符串
                   String responseString = new String(responseBody);
                   try {
                       JSONObject jsonResponse = new JSONObject(responseString);
                       Boolean loggedout=jsonResponse.getBoolean("loggedout");
                       if(loggedout) {
                           welcome.setText("欢迎你");
                           welcome.setVisibility(View.GONE);
                           loginBtn.setVisibility(View.VISIBLE);
                           div.setVisibility(View.VISIBLE);
                           registerBtn.setVisibility(View.VISIBLE);
                           exit.setVisibility(View.GONE);
                           deleteCookieFromSharedPreferences();
                       }else {
                           Toast.makeText(getActivity(), "退出登录失败", Toast.LENGTH_SHORT).show();
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
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }

    //删除cookie
    private void deleteCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("my_cookie_key"); // 删除名为 "my_cookie_key" 的 cookie
        editor.apply(); // 提交更改
    }

}