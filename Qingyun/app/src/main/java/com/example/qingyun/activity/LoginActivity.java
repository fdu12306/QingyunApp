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
    private EditText etPhone;
    private ImageView clearPhone;
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
        etPhone=findViewById(R.id.et_mobile);
        clearPhone=findViewById(R.id.iv_clean_phone);
        etPassword=findViewById(R.id.et_password);
        clearPassword=findViewById(R.id.iv_clean_password);
        showPassword=findViewById(R.id.iv_show_pwd);
        btnLogin=findViewById(R.id.btn_login);
        forgetPassword=findViewById(R.id.forget_password);
        returnRegister=findViewById(R.id.register);
    }

    private void initListener() {
        clearPhone.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        returnRegister.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && clearPhone.getVisibility() == View.GONE) {
                    clearPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(editable)) {
                    clearPhone.setVisibility(View.GONE);
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
        if (viewId==R.id.iv_clean_phone) {
            etPhone.setText("");
        } else if (viewId==R.id.iv_clean_password) {
            etPassword.setText("");
        } else if (viewId==R.id.iv_show_pwd) {
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
        } else if (viewId==R.id.btn_login) {//登录

        } else if (viewId==R.id.register) {
            finish();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else {//忘记密码

        }
    }
}