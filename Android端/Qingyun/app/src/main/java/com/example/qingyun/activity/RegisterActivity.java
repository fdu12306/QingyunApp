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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qingyun.R;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private final String registerUrl= AppConfig.BaseUrl+"/register.php";//后端注册url
    private EditText etUsername;
    private ImageView clearUsername;
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
        etUsername=findViewById(R.id.etUsername);
        clearUsername=findViewById(R.id.clearUsername);
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
        clearUsername.setOnClickListener(this);
        clearStudentId.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        clearConfirmPassword.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        showConfirmPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        returnLogin.setOnClickListener(this);
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable) && clearStudentId.getVisibility() == View.GONE) {
                    clearUsername.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(editable)) {
                    clearUsername.setVisibility(View.GONE);
                }
            }
        });
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
        if(viewId==R.id.clearUsername){//清除用户名
            etUsername.setText("");
        }else if(viewId==R.id.clearStudentId){//清除学号
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
            String username=etUsername.getText().toString().trim();
            String studentId=etStudentId.getText().toString().trim();
            String password=etPassword.getText().toString().trim();
            String confirmPassword=etConfirmPassword.getText().toString().trim();
            if(username.isEmpty()){
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            }else if(studentId.isEmpty()){
                Toast.makeText(this, "请输入学号", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            } else if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }else {
                RequestParams requestParams=new RequestParams();
                requestParams.put("username",username);
                requestParams.put("studentId",studentId);
                requestParams.put("password",password);
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                asyncHttpClient.post(registerUrl, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // 将 byte 数组转换为字符串
                        String responseString = new String(responseBody);
                        try {
                            // 解析字符串为 JSON 对象
                            JSONObject jsonResponse = new JSONObject(responseString);
                            // 在此处处理 jsonResponse，根据后端返回的数据结构进行操作
                            // 例如，如果后端返回了一个名为 "message" 的字段
                            String message = jsonResponse.getString("message");
                            int state=jsonResponse.getInt("state");
                            if(state==200){
                                finish();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
                                // 在 Toast 中显示失败消息
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(RegisterActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // 处理失败的情况
                        Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (viewId==R.id.returnLogin) {//返回登录
            finish();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}