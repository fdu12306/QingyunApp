package com.example.qingyun.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qingyun.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etStudentId;
    private ImageView clearStudentId;
    private EditText etPassword;
    private ImageView clearPassword;
    private ImageView showPassword;
    private Button btnLogin;
    private TextView forgetPassword;
    private TextView returnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    private void initView(){
        etStudentId=findViewById(R.id.etStudentId);
        clearStudentId=findViewById(R.id.clearStudentId);
        etPassword=findViewById(R.id.etPassword);
        clearPassword=findViewById(R.id.clearPassword);
        showPassword=findViewById(R.id.showPassword);
        btnLogin=findViewById(R.id.login);
        forgetPassword=findViewById(R.id.forgetPassword);
        returnRegister=findViewById(R.id.returnRegister);
    }

    private void initListener() {
        clearStudentId.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        returnRegister.setOnClickListener(this);
        etStudentId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && clearStudentId.getVisibility() == View.GONE) {
                    clearStudentId.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(editable)) {
                    clearStudentId.setVisibility(View.GONE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && clearPassword.getVisibility() == View.GONE) {
                    clearPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(editable)) {
                    clearPassword.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if (viewId==R.id.clearStudentId) {//清除学号
            etStudentId.setText("");
        } else if (viewId==R.id.clearPassword) {//清除密码
            etPassword.setText("");
        } else if (viewId==R.id.showPassword) {//显示密码
            if (etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPassword.setImageResource(R.mipmap.pass_visuable);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPassword.setImageResource(R.mipmap.pass_gone);
            }
            String pwd = etPassword.getText().toString();
            if (!TextUtils.isEmpty(pwd)) {
                etPassword.setSelection(pwd.length());
            }
        } else if (viewId==R.id.login) {//登录

        } else if (viewId==R.id.returnRegister) {//返回注册
            finish();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else {//忘记密码

        }
    }
}