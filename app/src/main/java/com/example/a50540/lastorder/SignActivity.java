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
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignActivity extends Activity {
    ImageView btn_return1;

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


    }

    public void init(){

        btn_return1 = (ImageView)findViewById(R.id.btn_return1);
    }

    public void sign(){
        Map map = new HashMap();

        JSONObject jsonObject = new JSONObject(map);
        RequestBody requestBody = RequestBody.create(null,jsonObject.toString());
        Request request = new Request.Builder().url(MainActivity.ip+"signServlet").post(requestBody).build();
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
                    if (msg.obj.toString().equals("注册成功")){
                        Toast.makeText(SignActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignActivity.this,LoginActivity.class);
//                        intent.putExtra("username",username);
//                        intent.putExtra("password",password);
                        startActivity(intent);
                        finish();
                    }else
                        Toast.makeText(SignActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
