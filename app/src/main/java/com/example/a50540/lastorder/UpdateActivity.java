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

public class UpdateActivity extends Activity {
    Button btn_update;
    ImageView btn_return3;
    EditText et_username,et_pwd,et_repwd;
    String username,name,id_card,new_pwd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        et_username = (EditText)findViewById(R.id.et_update_user);
        et_pwd = (EditText)findViewById(R.id.et_update_pwd);
        et_repwd = (EditText)findViewById(R.id.et_update_repwd);
        btn_return3 = (ImageView) findViewById(R.id.btn_return3);
        btn_update = (Button) findViewById(R.id.btn_update);

        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        id_card = getIntent().getStringExtra("id_card");
        et_username.setText(username);

        btn_return3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateActivity.this,LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_pwd = et_pwd.getText().toString().trim();
                if (new_pwd.equals(et_repwd.getText().toString().trim())) {
                    update();

                    Intent intent = new Intent(UpdateActivity.this, LoginActivity.class);
                    intent.putExtra("old_user", username);
                    intent.putExtra("new_pwd", et_pwd.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }else
                    Toast.makeText(UpdateActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(){
        Map map = new HashMap();
        map.put("username",username);
        map.put("new_pwd",new_pwd);
        map.put("name",name);
        map.put("id_card",id_card);
        JSONObject jsonObject = new JSONObject(map);
        RequestBody requestBody = RequestBody.create(null,jsonObject.toString());
        final Request request = new Request.Builder().url(MainActivity.ip+"updateServlet").post(requestBody).build();
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
                    Toast.makeText(UpdateActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
