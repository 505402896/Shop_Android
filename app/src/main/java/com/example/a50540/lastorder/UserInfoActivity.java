package com.example.a50540.lastorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.util.Common;
import com.example.a50540.lastorder.util.Result;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONArray;
import org.json.JSONException;
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

public class UserInfoActivity extends AppCompatActivity {
  ImageView btn_return;
  QMUIRoundButton btn_update;
  TextView tv_name,tv_username;
  EditText et_dept,et_qq,et_address,et_phone;
  int uid;
  String username,name;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_userinfo);
    init();

    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
    uid = pref.getInt("uid",0);
    name = pref.getString("name", null);
    username = pref.getString("username", null);
    tv_name.setText(name);
    tv_username.setText(username);
    initData();
    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    btn_update.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        update();
      }
    });
  }

  private void init() {
    btn_return = (ImageView)findViewById(R.id.userinfo_return);
    btn_update = (QMUIRoundButton)findViewById(R.id.userinfo_btn_update);
    tv_username = (TextView)findViewById(R.id.userinfo_tv_username);
    tv_name = (TextView)findViewById(R.id.userinfo_tv_name);
    et_dept = (EditText)findViewById(R.id.userinfo_et_dept);
    et_phone = (EditText)findViewById(R.id.userinfo_et_phone);
    et_address = (EditText)findViewById(R.id.userinfo_et_address);
    et_qq = (EditText)findViewById(R.id.userinfo_et_qq);
  }

  public void initData() {
    String url = Common.SERVER_URL +"/user/getUserInfo?uid="+uid;

    final Request request = new Request.Builder().url(url).build();
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Log.d("tag",e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        try {
          JSONObject jsonObject = new JSONObject(response.body().string());

          Result result = new Result();
          result.setCode(jsonObject.getInt("code"));
          result.setMessage(jsonObject.getString("message"));
          result.setData(jsonObject.get("data"));
          Message msg = handler.obtainMessage();
          msg.what = 2;
          Bundle bundle = new Bundle();
          bundle.putSerializable("result",result);
          msg.setData(bundle);
          handler.sendMessage(msg);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void update() {
    Map<String,Object> map = new HashMap<>();
    map.put("uid",uid);
    map.put("dept", et_dept.getText().toString());
    map.put("phone", et_phone.getText().toString());
    map.put("qqNumber", et_qq.getText().toString());
    map.put("address", et_address.getText().toString());

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String url = Common.SERVER_URL +"/user/updateUserInfo";

    JSONObject jsonObject = new JSONObject(map);

    RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
    final Request request = new Request.Builder().url(url).post(requestBody).build();
    OkHttpClient okHttpClient = new OkHttpClient();
    Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        Log.d("tag",e.getMessage());
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

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          Toast.makeText(UserInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
          finish();
          break;
        case 2:
          Bundle bundle = msg.getData();
          Result result = (Result) bundle.getSerializable("result");
          JSONObject jsonObject = (JSONObject) result.getData();
          try {
            et_dept.setText(jsonObject.getString("dept"));
            et_address.setText(jsonObject.getString("address"));
            et_phone.setText(jsonObject.getString("phone"));
            et_qq.setText(jsonObject.getString("qqNumber"));
          } catch (JSONException e) {
            e.printStackTrace();
          }
          break;
      }
    }
  };
}
