package com.example.a50540.lastorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.util.Common;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignActivity extends Activity {
    ImageView btn_return1;
    TextView tv_username,tv_password,tv_repwd,tv_name;
    Button btn_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        init();

        btn_return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);    //第一个参数是要跳转的屏幕，第二个参数是本屏幕
                finish();
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_password.getText().toString().equals(tv_repwd.getText().toString())) {
                    sign();
                } else {
                    Toast.makeText(SignActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void init(){
        tv_username = (TextView)findViewById(R.id.tv_sign_user);
        tv_password = (TextView)findViewById(R.id.et_sign_pwd);
        tv_repwd = (TextView)findViewById(R.id.et_sign_repwd);
        tv_name = (TextView)findViewById(R.id.tv_sign_name);
        btn_sign = (Button)findViewById(R.id.btn_sign);
        btn_return1 = (ImageView)findViewById(R.id.btn_return1);
    }

    public void sign(){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = Common.SERVER_URL + "/user/sign";
        Map<String,Object> map = new HashMap<>();
        map.put("username", tv_username.getText().toString());
        map.put("password",tv_password.getText().toString());
        map.put("name",tv_name.getText().toString());
        JSONObject jsonObject = new JSONObject(map);
        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder().url(url).put(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    if (msg.obj.toString().equals("注册成功")){
//                        Toast.makeText(SignActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SignActivity.this,LoginActivity.class);
//                        intent.putExtra("username",username);
//                        intent.putExtra("password",password);
//                        startActivity(intent);
//                        finish();
//                    }else
                        Toast.makeText(SignActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
