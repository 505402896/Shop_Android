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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.adapter.ChatAdapter;
import com.example.a50540.lastorder.util.Common;
import com.example.a50540.lastorder.util.Result;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chat extends AppCompatActivity {
  ImageView btn_return1;
  int sendId,acceptId;
  String name;
  TextView tv_name;
  ListView listView;
  Map<String,Object> map;
  List<Map<String,Object>> list;
  ChatAdapter chatAdapter;
  QMUIRoundButton btn_send;
  EditText et_content;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    init();
    chatAdapter = new ChatAdapter(getData(),this,R.layout.chat_list);
    listView.setAdapter(chatAdapter);

//    获取收信息人的id
    Intent intent = getIntent();
    acceptId = intent.getIntExtra("uid",0);
    name = intent.getStringExtra("name");
    tv_name.setText(name);

//    获取发送信息人的id
    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
    sendId = pref.getInt("uid",0);

//    获取聊天信息
    accept();

    btn_return1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    btn_send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        send();
      }
    });
  }

  public void init() {
    et_content = (EditText) findViewById(R.id.chat_et_content);
    btn_send = (QMUIRoundButton) findViewById(R.id.btn_chat_send);
    btn_return1 = (ImageView) findViewById(R.id.chat_btn_return1);
    tv_name = (TextView) findViewById(R.id.chat_tv_name);
    listView = (ListView) findViewById(R.id.chat_list);
  }

  private List<Map<String,Object>> getData() {
    list = new ArrayList<Map<String, Object>>();
    map = new HashMap<>();
    return list;
  }

  public void send() {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String url = Common.SERVER_URL +"/chat/addMessage";

    Map<String,Object> map = new HashMap<>();
    map.put("sendId",sendId);
    map.put("acceptId",acceptId);
    map.put("content",et_content.getText().toString());
    JSONObject jsonObject = new JSONObject(map);

    RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
    final Request request = new Request.Builder().url(url).put(requestBody).build();
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

  public void accept() {
    String url = Common.SERVER_URL + "/chat/getMessage?sendId="+sendId+"&acceptId="+acceptId;
    OkHttpClient okHttpClient = new OkHttpClient();
    final Request request = new Request.Builder()
            .url(url)
            .build();
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

  Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          map = new HashMap<>();
          map.put("position","right");
          map.put("content",et_content.getText().toString());
          list.add(map);
          chatAdapter.notifyDataSetChanged();
          et_content.setText("");
          break;
        case 2:
          Bundle bundle = msg.getData();
          Result result = (Result) bundle.getSerializable("result");
          JSONArray jsonArray = (JSONArray) result.getData();
          System.out.println(jsonArray.toString());
          try {
            for (int i = 0; i < jsonArray.length(); i++) {
              if(jsonArray.getJSONObject(i).get("sendId").equals(sendId)) {
//                判断是否是自己发出
                map = new HashMap<>();
                map.put("position","right");
//                map.put("image",R.drawable.genshin);
                map.put("content",jsonArray.getJSONObject(i).get("content"));
                list.add(map);
              } else {
                map = new HashMap<>();
                map.put("position","left");
//                map.put("image",R.drawable.head_icon);
                map.put("content",jsonArray.getJSONObject(i).get("content"));
                list.add(map);
              }
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          chatAdapter.notifyDataSetChanged();
          break;
      }
    }
  };
}
