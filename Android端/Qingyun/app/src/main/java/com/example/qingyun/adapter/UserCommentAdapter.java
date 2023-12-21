package com.example.qingyun.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qingyun.R;
import com.example.qingyun.activity.ProductDetailsActivity;
import com.example.qingyun.bean.Comment;
import com.example.qingyun.utils.AppConfig;

import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.ViewHolder>{
    private List<Comment> commentList;
    private Context context;
    private static final String getUserCommentUrl= AppConfig.BaseUrl+"/getUserComment.php";
    public UserCommentAdapter(List<Comment> commentList,Context context){
        this.commentList=commentList;
        this.context=context;
    }
    @NonNull
    @Override
    public UserCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usercomment, parent, false);
        return new UserCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentAdapter.ViewHolder holder, int position) {
        Comment comment=commentList.get(position);

        holder.textViewCommentTime.setText("评论时间："+comment.getIssueTime());
        holder.textViewCommentContent.setText("评论内容："+comment.getContent());

        Button buttonCheck=holder.itemView.findViewById(R.id.buttonCheck);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productId", comment.getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCommentTime;
        TextView textViewCommentContent;
        Button buttonCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
            textViewCommentTime = itemView.findViewById(R.id.textViewCommentTime);
            buttonCheck = itemView.findViewById(R.id.buttonCheck);
        }
    }
}
