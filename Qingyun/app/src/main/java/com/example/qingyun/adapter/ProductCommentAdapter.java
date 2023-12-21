package com.example.qingyun.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingyun.R;
import com.example.qingyun.bean.Comment;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ProductCommentAdapter extends RecyclerView.Adapter<ProductCommentAdapter.ViewHolder>{
    private List<Comment> commentList;
    private Context context;
    private static final String deleteCommentUrl= AppConfig.BaseUrl+"/deleteComment.php";

    public ProductCommentAdapter(List<Comment> commentList, Context context){
        this.commentList=commentList;
        this.context=context;
    }

    @NonNull
    @Override
    public ProductCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ProductCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCommentAdapter.ViewHolder holder, int position) {
        Comment comment=commentList.get(position);
        holder.textViewUserName.setText(comment.getUsername()+"：");
        if(comment.getDeleteState()==1){
            holder.textViewCommentContent.setText("此评论已删除！");
            holder.textViewCommentContent.setTextColor(Color.parseColor("#c61a04"));
        }
        else {
            holder.textViewCommentContent.setText(comment.getContent());
        }
        holder.textViewCommentTime.setText(comment.getIssueTime());
        Button buttonDeleteComment=holder.itemView.findViewById(R.id.buttonDeleteComment);
        if(comment.getIsOwner()==1&&comment.getDeleteState()==0){
            buttonDeleteComment.setVisibility(View.VISIBLE);
        }
        else{
            buttonDeleteComment.setVisibility(View.GONE);
        }
        buttonDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                String myCookie = loadCookieFromSharedPreferences();
                asyncHttpClient.addHeader("Cookie", myCookie);
                RequestParams requestParams = new RequestParams();
                requestParams.put("id",comment.getId());
                asyncHttpClient.post(deleteCommentUrl, requestParams, new AsyncHttpResponseHandler() {
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
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        TextView textViewCommentContent;
        TextView textViewCommentTime;
        Button buttonDeleteComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
            textViewCommentTime = itemView.findViewById(R.id.textViewCommentTime);
            buttonDeleteComment = itemView.findViewById(R.id.buttonDeleteComment);
        }
    }

    // 自定义方法，从 SharedPreferences 中加载 Cookie
    private String loadCookieFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("MyCookiePreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("my_cookie_key", "");
    }
}
