package com.example.qingyun.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingyun.R;
import com.example.qingyun.bean.Message;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private List<Message> messageList;
    private Context context;
    private static final String readMessageUrl=AppConfig.BaseUrl+"/readMessage.php";
    private static final String deleteMessageUrl=AppConfig.BaseUrl+"/deleteMessage.php";
    public MessageAdapter(List<Message> messageList,Context context){
        this.messageList=messageList;
        this.context=context;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message=messageList.get(position);

        holder.textViewMessageTime.setText("消息时间："+message.getTime());
        holder.textViewMessageRead.setText("是否已读："+(message.getIsRead()==1?"是":"否"));
        holder.textViewMessageContent.setText("消息内容："+message.getRemark());

        // 标记已读按钮点击事件
        Button readButton = holder.itemView.findViewById(R.id.buttonIsRead);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                RequestParams requestParams = new RequestParams();
                requestParams.put("id",message.getId());
                asyncHttpClient.post(readMessageUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 将 byte 数组转换为字符串
                        String responseString = new String(responseBody);
                        try{
                            JSONObject jsonResponse = new JSONObject(responseString);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(context, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 删除消息按钮点击事件
        Button deleteButton = holder.itemView.findViewById(R.id.buttonDeleteMessage);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                RequestParams requestParams = new RequestParams();
                requestParams.put("id",message.getId());
                asyncHttpClient.post(deleteMessageUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 将 byte 数组转换为字符串
                        String responseString = new String(responseBody);
                        try{
                            JSONObject jsonResponse = new JSONObject(responseString);
                            String message = jsonResponse.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(context, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessageTime;
        TextView textViewMessageRead;
        TextView textViewMessageContent;
        Button buttonIsRead;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageTime=itemView.findViewById(R.id.textViewMessageTime);
            textViewMessageRead=itemView.findViewById(R.id.textViewMessageRead);
            textViewMessageContent=itemView.findViewById(R.id.textViewMessageContent);
            buttonIsRead=itemView.findViewById(R.id.buttonIsRead);
            buttonDelete=itemView.findViewById(R.id.buttonDelete);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}
