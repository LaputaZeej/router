package com.laputa.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.laputa.annotations.HField;
import com.laputa.annotations.HRouter;
import com.laputa.rounter.api.RouterManager;

@HRouter(path = "/login/LoginActivity")
public class LoginActivity extends AppCompatActivity {


    @HField(name = "name1")
    public String name;

    @HField
    public int a;

    @HField
    public long b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);
        RouterManager.getInstance().load(this);
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                TextView info = findViewById(R.id.tv_info);
                String msg = "name = " + name + " , a = " + a + " ,b = " + b;
                Log.i("xxxxx", msg + " , ->");
                info.setText(msg);
                return false;
            }
        });
        findViewById(R.id.btn_to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().create("/person/PersonActivity")
                        .with("msg", "hello,p! i am from login")
                        .navigation(LoginActivity.this);

            }
        });

    }
}