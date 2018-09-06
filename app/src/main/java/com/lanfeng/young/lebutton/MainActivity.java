package com.lanfeng.young.lebutton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import so.chinaso.com.voicemodule.VoiceActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private List<String> textList = new ArrayList<>();
    private RecyclerView mR;

    //录音权限
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        mR = findViewById(R.id.answer_rcy);
        for (int i = 0; i < 4; i++) {
            textList.add("我是你的的等你我是你的的等你我是你的的等你我是你的的等你我是你的的等你");
        }
        AnAdapter adapter = new AnAdapter(this,textList);
        mR.setLayoutManager(new LinearLayoutManager(this));
        mR.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,VoiceActivity.class));
            }
        });
    }

    //申请录音权限
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PERMISSION_GRANTED) {
                    this.finish();
                }
            }
        }
    }
}
