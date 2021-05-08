package com.example.a50540.lastorder;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.util.AsyncBitmapLoader;
import com.example.a50540.lastorder.util.Common;
import com.example.a50540.lastorder.util.Result;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);
    asyncBitmapLoader=new AsyncBitmapLoader();

//    初始化组件
    init();

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
  }

  public void init() {
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
                tv_type.setText("护士用品");
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
      }
    }
  };
}
