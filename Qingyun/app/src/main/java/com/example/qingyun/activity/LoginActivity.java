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
import android.widget.Toast;

import com.example.qingyun.MainActivity;
import com.example.qingyun.R;
import com.example.qingyun.utils.AppConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etStudentId;
    private ImageView clearStudentId;
    private EditText etPassword;
    private ImageView clearPassword;
    private ImageView showPassword;
    private Button btnLogin;
    private TextView forgetPassword;
    private TextView returnRegister;
    private static final String loginUrl= AppConfig.BaseUrl+"/login.php";

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
        forgetPassword.setOnClickListener(this);
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
            String studentId=etStudentId.getText().toString().trim();
            String password=etPassword.getText().toString().trim();
            if(studentId.isEmpty()){
                Toast.makeText(LoginActivity.this, "请输入学号", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            }else {
                RequestParams requestParams=new RequestParams();
                requestParams.put("studentId",studentId);
                requestParams.put("password",password);
                AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
                asyncHttpClient.post(loginUrl, requestParams, new AsyncHttpResponseHandler() {
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
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                // 在 Toast 中显示失败消息
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // JSON 解析失败，可以在这里处理异常
                            Toast.makeText(LoginActivity.this, "JSON 解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // 处理失败的情况
                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (viewId==R.id.returnRegister) {//返回注册
            finish();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else if(viewId==R.id.forgetPassword) {//忘记密码
            Toast.makeText(LoginActivity.this, "请联系系统管理员", Toast.LENGTH_SHORT).show();
        }
    }
}