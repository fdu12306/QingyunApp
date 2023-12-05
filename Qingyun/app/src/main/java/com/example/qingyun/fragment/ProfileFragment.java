package com.example.qingyun.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qingyun.MainActivity;
import com.example.qingyun.R;
import com.example.qingyun.activity.LoginActivity;
import com.example.qingyun.activity.RegisterActivity;
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
    private TextView welcom;
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
        welcom=view.findViewById(R.id.welcome);
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
        initDisplay();
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

    public void initDisplay(){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        asyncHttpClient.get(checkLoginUrl, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // 将 byte 数组转换为字符串
                String responseString = new String(responseBody);
                Log.d("Response", responseString); // 添加日志
                try {
                    JSONObject jsonResponse = new JSONObject(responseString);
                    Boolean logged_in=jsonResponse.getBoolean("logged");
                    if(logged_in) {
                        String username=jsonResponse.getString("username");
                        welcom.setText("欢迎你，"+username+"!");
                        welcom.setVisibility(View.VISIBLE);
                        loginBtn.setVisibility(View.GONE);
                        div.setVisibility(View.GONE);
                        registerBtn.setVisibility(View.GONE);
                        exit.setVisibility(View.VISIBLE);
                    }else {
                        welcom.setVisibility(View.GONE);
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

       } else if (viewId==R.id.browser) {//浏览

       } else if (viewId==R.id.comment) {//评论

       } else if (viewId==R.id.issue) {//发布

       } else if (viewId==R.id.sold) {//已卖出

       } else if (viewId==R.id.order) {//已下单

       } else if (viewId==R.id.exit) {//退出登录

       }
    }
}