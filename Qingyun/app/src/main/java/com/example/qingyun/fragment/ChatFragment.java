package com.example.qingyun.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qingyun.R;
import com.example.qingyun.adapter.MessageAdapter;
import com.example.qingyun.adapter.ProductAdapter;
import com.example.qingyun.bean.Message;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerViewMessage;
    private static List<Message> messageList;
    private static final String getMessageUrl= AppConfig.BaseUrl+"/getMessage.php";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerViewMessage=view.findViewById(R.id.recyclerViewMessage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMessageData();
    }

    private void loadMessageData(){
        messageList=new ArrayList<>();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        RequestParams requestParams = new RequestParams(); // 如果有查询参数，可以在这里添加
        String myCookie = loadCookieFromSharedPreferences();
        asyncHttpClient.addHeader("Cookie", myCookie);
        asyncHttpClient.get(getMessageUrl, requestParams, new AsyncHttpResponseHandler() {
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
                            String time=item.getString("time");
                            String remark=item.getString("remark");
                            int isRead=item.getInt("isRead");
                            Message message=new Message(id,time,remark,isRead);
                            messageList.add(message);
                        }
                        // 数据加载完成后初始化适配器并设置给 recyclerViewProducts
                        MessageAdapter messageAdapter = new MessageAdapter(messageList,requireContext());
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        int verticalSpacingInPixels = 10;
                        recyclerViewMessage.addItemDecoration(new VerticalSpaceItemDecoration(verticalSpacingInPixels));
                        recyclerViewMessage.setLayoutManager(gridLayoutManager);
                        recyclerViewMessage.setAdapter(messageAdapter);
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

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}