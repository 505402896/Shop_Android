package com.example.a50540.lastorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.util.Common;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

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

import static android.content.Context.MODE_PRIVATE;

public class Fifth extends Fragment {

  TextView tv_username,tv_name;
  LinearLayout btn_my_good,btn_update_password,btn_logout,btn_userinfo;
  String username,name;
  int uid;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fifth,container,false);
    tv_username = (TextView)view.findViewById(R.id.fifth_tv_username);
    tv_name = (TextView)view.findViewById(R.id.fifth_tv_name);

    final SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
    username = pref.getString("username",null);
    name = pref.getString("name",null);
    uid = pref.getInt("uid",0);
    String info = "账号："+username;

    tv_username.setText(info);
    tv_name.setText(name);

    btn_my_good = (LinearLayout) view.findViewById(R.id.btn_my_good);
    btn_update_password = (LinearLayout) view.findViewById(R.id.fifth_btn_update_password);
    btn_logout = (LinearLayout) view.findViewById(R.id.fifth_btn_logout);
    btn_userinfo = (LinearLayout)view.findViewById(R.id.fifth_btn_userinfo);

    btn_userinfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(),UserInfoActivity.class);
        startActivity(intent);
      }
    });

    btn_logout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        pref.edit().clear();
        startActivity(intent);
      }
    });

    btn_update_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("修改密码")
                .setPlaceholder("请输入新密码")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                  @Override
                  public void onClick(QMUIDialog qmuiDialog, int i) {
                    qmuiDialog.dismiss();
                  }
                }).addAction("确认修改", new QMUIDialogAction.ActionListener() {
          @Override
          public void onClick(QMUIDialog qmuiDialog, int i) {
            String newPassword = builder.getEditText().getText().toString();
            updatePassword(newPassword);
            qmuiDialog.dismiss();
          }
        }).show();
      }
    });

    btn_my_good.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(),MyGood.class);
        startActivity(intent);
      }
    });
    return view;
  }

  private void updatePassword(String password) {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String url = Common.SERVER_URL +"/user/updatePassword";

    Map<String,Object> map = new HashMap<>();
    map.put("uid", uid);
    map.put("password", password);
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

  Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
          break;
      }
    }
  };
}