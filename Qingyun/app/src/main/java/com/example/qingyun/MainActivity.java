package com.example.qingyun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.qingyun.activity.LoginActivity;
import com.example.qingyun.activity.RegisterActivity;
import com.example.qingyun.fragment.ChatFragment;
import com.example.qingyun.fragment.HomeFragment;
import com.example.qingyun.fragment.ProfileFragment;
import com.example.qingyun.fragment.PublishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //启动时为Home页面
        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container, homeFragment)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 处理导航项的选择事件
                int itemId=item.getItemId();
                if(itemId==R.id.navigation_home){//首页
                    switchFragment(new HomeFragment());
                    return true;
                } else if (itemId==R.id.navigation_publish) {//发布
                    switchFragment(new PublishFragment());
                    return true;
                } else if (itemId==R.id.navigation_chat) {//聊天
                    switchFragment(new ChatFragment());
                    return true;
                } else if (itemId==R.id.navigation_profile) {//个人中心
                    switchFragment(new ProfileFragment());
                    return true;
                }else {
                    return false;
                }
            }
        });
    }

    //切换不同的fragment
    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}