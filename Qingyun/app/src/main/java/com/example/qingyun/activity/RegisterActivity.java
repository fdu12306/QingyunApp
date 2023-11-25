package com.example.qingyun.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.qingyun.R;
import com.example.qingyun.utils.AppConfig;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private final String registerUrl= AppConfig.BaseUrl+"register.php";//后端注册url

    private EditText etStudentId;
    private ImageView clearStudentId;
    private EditText etPassword;
    private ImageView clearPassword;
    private ImageView showPassword;
    private EditText etConfirmPassword;
    private ImageView clearConfirmPassword;
    private ImageView showConfirmPassword;
    private Button btnRegister;
    private TextView returnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }

    private void initView(){
        etStudentId=findViewById(R.id.etStudentId);
        clearStudentId=findViewById(R.id.clearStudentId);
        etPassword=findViewById(R.id.etPassword);
        clearPassword=findViewById(R.id.clearPassword);
        showPassword=findViewById(R.id.showPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        clearConfirmPassword=findViewById(R.id.clearConfirmPassword);
        showConfirmPassword=findViewById(R.id.showConfirmPassword);
        btnRegister=findViewById(R.id.register);
        returnLogin=findViewById(R.id.returnLogin);
    }

    private void initListener() {
        clearStudentId.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        clearConfirmPassword.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        returnLogin.setOnClickListener(this);
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
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && clearConfirmPassword.getVisibility() == View.GONE) {
                    clearConfirmPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(editable)) {
                    clearConfirmPassword.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if(viewId==R.id.clearStudentId){//清除学号
            etStudentId.setText("");
        } else if (viewId==R.id.clearPassword) {//清除密码
            etPassword.setText("");
        } else if (viewId==R.id.clearConfirmPassword) {//清楚确认密码
            etConfirmPassword.setText("");
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
        }else if (viewId==R.id.showConfirmPassword) {//显示确认密码
            if (etConfirmPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showConfirmPassword.setImageResource(R.mipmap.pass_visuable);
            } else {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showConfirmPassword.setImageResource(R.mipmap.pass_gone);
            }
            String pwd = etConfirmPassword.getText().toString();
            if (!TextUtils.isEmpty(pwd)) {
                etConfirmPassword.setSelection(pwd.length());
            }
        } else if (viewId==R.id.register) {//注册

        } else if (viewId==R.id.returnLogin) {//返回登录
            finish();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}