package com.example.a50540.lastorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.adapter.CommentAdapter;
import com.example.a50540.lastorder.util.AsyncBitmapLoader;
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

public class GoodDetailActivity extends AppCompatActivity {
  ImageView btn_return,image;
  TextView tv_name,tv_price,tv_detail,tv_type;
  int uid,gid;
  String name;
  QMUIRoundButton btn_toChat,btn_comment;
  private AsyncBitmapLoader asyncBitmapLoader;
  LinearLayout btn_toComment;
  EditText et_comment;
  Map<String,Object> map;
  List<Map<String,Object>> list;
  ListView listView;
  CommentAdapter commentAdapter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);
    asyncBitmapLoader=new AsyncBitmapLoader();

//    初始化组件
    init();

    commentAdapter = new CommentAdapter(getData(),GoodDetailActivity.this,R.layout.comment_item);
    listView.setAdapter(commentAdapter);

    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });

    final Intent intent = getIntent();
    gid = intent.getIntExtra("gid",0);

//    初始化数据
    initData(gid);
    initComment(gid);

    btn_toChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent toChat = new Intent(GoodDetailActivity.this,Chat.class);
//        要聊天人的id
        toChat.putExtra("uid", uid);
        toChat.putExtra("name",name);
        startActivity(toChat);
      }
    });

    btn_toComment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        et_comment.setVisibility(View.VISIBLE);
        btn_comment.setVisibility(View.VISIBLE);
        btn_toChat.setVisibility(View.INVISIBLE);
        btn_toComment.setVisibility(View.INVISIBLE);
      }
    });

    btn_comment.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addComment();
      }
    });
  }

  private void addComment() {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String url = Common.SERVER_URL +"/comment/addComment";
    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
    int uid = pref.getInt("uid",0);
    String name = pref.getString("name",null);
    Map<String,Object> map = new HashMap<>();
    map.put("gid",gid);
    map.put("uid",uid);
    map.put("name",name);
    map.put("content",et_comment.getText().toString());
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
        msg.what = 3;
        msg.obj = response.body().string();
        handler.sendMessage(msg);
      }
    });
  }

  private List<Map<String,Object>> getData() {
    list = new ArrayList<Map<String, Object>>();
    map = new HashMap<>();
    return list;
  }

  public void init() {
    listView = (ListView)findViewById(R.id.comment_list);
    et_comment = (EditText)findViewById(R.id.detail_et_comment);
    btn_comment = (QMUIRoundButton)findViewById(R.id.detail_btn_comment);
    btn_toComment = (LinearLayout)findViewById(R.id.detail_btn_toComment);
    btn_toChat = (QMUIRoundButton)findViewById(R.id.detail_btn_toChat);
    tv_type = (TextView)findViewById(R.id.detail_classify);
    tv_detail = (TextView)findViewById(R.id.detail_detail);
    tv_name = (TextView) findViewById(R.id.detail_name);
    tv_price = (TextView)findViewById(R.id.detail_price);
    btn_return = (ImageView)findViewById(R.id.detail_btn_return1);
    image = (ImageView)findViewById(R.id.detail_image);
  }

  public void initData(int gid) {
    String url = Common.SERVER_URL + "/goods/getGoodsById?gid="+gid;
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
          JSONObject data = jsonObject.getJSONObject("data");
          Result result = new Result();
          result.setData(data);
          Message msg = handler.obtainMessage();
          msg.what = 1;
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

  private void initComment(int gid) {
//    定义去后台取数据的接口地址
    String url = Common.SERVER_URL + "/comment/getComment?gid="+gid;
//    新建一个http链接 并绑定url
    OkHttpClient okHttpClient = new OkHttpClient();
    final Request request = new Request.Builder()
            .url(url)
            .build();
//    接受当前http请求返回的情况： 失败或者成功 成功即取数据
    Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
//        失败即打印错误信息
        Log.d("tag",e.getMessage());
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        try {
//          成功即接受数据  注意前后端通信格式为JSON格式 需要把收到的信息 response 转成JSON
          JSONObject jsonObject = new JSONObject(response.body().string());
          Result result = new Result();
          result.setData(jsonObject.get("data"));
//          因为在http请求这种耗时操作 可以理解为网络请求 这种操作中
//          不可以直接更改界面内容 会报错
//          所以需要一个Handler 类似中间人  去帮你完成这些操作
//          Message 是用来存储你要交给这个中间人的 一些数据 在这里就是我们获取到的所有评论
          Message msg = handler.obtainMessage();
          msg.what = 2;
//          Message不能直接存储JSON 所以用Bundle打包一下
//          有点套娃 总之是为了让他能存储我们的评论  不用去记！！
          Bundle bundle = new Bundle();
          bundle.putSerializable("result",result);
//          最后给message设置好信息 并绑定handler
          msg.setData(bundle);
          handler.sendMessage(msg);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          Bundle bundle = msg.getData();
          Result result = (Result) bundle.getSerializable("result");
          JSONObject jsonObject = (JSONObject) result.getData();

          try {
            uid = jsonObject.getInt("uid");
            name = jsonObject.getString("name");
            tv_name.setText(jsonObject.getString("name"));
            tv_price.setText(String.valueOf(jsonObject.getInt("price")));
            tv_detail.setText(jsonObject.getString("detail"));
            switch (jsonObject.getInt("type")){
              case 1:
                tv_type.setText("学习用品");
                break;
              case 2:
                tv_type.setText("生活用品");
                break;
              case 3:
                tv_type.setText("体育用品");
                break;
              case 4:
                tv_type.setText("电子设备");
                break;
              case 5:
                tv_type.setText("食品分类");
                break;
              case 6:
                tv_type.setText("护肤用品");
                break;
            }

            String imageURL = Common.IMAGE_BASE_PATH + jsonObject.getString("pic");
            Bitmap bitmap=asyncBitmapLoader.loadBitmap(image, imageURL, new AsyncBitmapLoader.ImageCallBack() {
              @Override
              public void imageLoad(ImageView imageView, Bitmap bitmap) {
                // TODO Auto-generated method stub
                imageView.setImageBitmap(bitmap);
              }
            });
            if(bitmap == null)
            {
              image.setImageResource(R.mipmap.loading);
            }
            else
            {
              image.setImageBitmap(bitmap);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          break;
        case 2:
          list.clear();
          Bundle bundle1 = msg.getData();
          Result comment = (Result) bundle1.getSerializable("result");
//          获取数据并转成JSON数组 进行循环  每个循环就是一条评论 将评论添加到map  再将map添加到list中
          JSONArray jsonArray = (JSONArray) comment.getData();
          try {
            for(int i = 0;i < jsonArray.length(); i++) {
              Map<String, Object> map = new HashMap<>();
              map.put("name",jsonArray.getJSONObject(i).get("name"));
              map.put("content", jsonArray.getJSONObject(i).get("content"));
              map.put("time", jsonArray.getJSONObject(i).get("createTime"));
              list.add(map);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
//          刷新适配器新加的数据
          commentAdapter.notifyDataSetChanged();
          break;
        case 3:
          Toast.makeText(GoodDetailActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
          initComment(gid);
          et_comment.setText("");
          break;
      }
    }
  };
}
