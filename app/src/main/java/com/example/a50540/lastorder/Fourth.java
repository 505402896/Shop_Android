package com.example.a50540.lastorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.a50540.lastorder.adapter.RecentAdapter;
import com.example.a50540.lastorder.util.Common;
import com.example.a50540.lastorder.util.Result;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Fourth extends Fragment {
  ListView listView;
  RecentAdapter recentAdapter;
  Map<String,Object> map;
  List<Map<String,Object>> list;
  int uid;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fourth,container,false);

    listView = (ListView) view.findViewById(R.id.fourth_list);
    recentAdapter = new RecentAdapter(getData(),getActivity(),R.layout.recent_list);
    listView.setAdapter(recentAdapter);

//    获取当前登录人的id
    SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
    uid = pref.getInt("uid",0);
    initData();
    return view;
  }

  private List<Map<String,Object>> getData() {
    list = new ArrayList<Map<String, Object>>();
    map = new HashMap<>();
    return list;
  }

  public void initData() {
    String url = Common.SERVER_URL + "/recent/getRecent?uid="+uid;
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
          result.setCode(jsonObject.getInt("code"));
          result.setMessage(jsonObject.getString("message"));
          result.setData(jsonObject.get("data"));

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

  Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 1:
          Bundle bundle = msg.getData();
          Result result = (Result) bundle.getSerializable("result");
          JSONArray jsonArray = (JSONArray) result.getData();
          try {
            for(int i = 0;i < jsonArray.length(); i++) {
              int targetId = jsonArray.getJSONObject(i).getInt("sendId") == uid ? jsonArray.getJSONObject(i).getInt("acceptId") : jsonArray.getJSONObject(i).getInt("sendId");
              Map<String, Object> map = new HashMap<>();
              map.put("targetId", targetId);
              map.put("name",jsonArray.getJSONObject(i).get("name"));
              map.put("content", jsonArray.getJSONObject(i).get("content"));
              map.put("time", jsonArray.getJSONObject(i).get("updateTime"));
              list.add(map);
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
          recentAdapter.notifyDataSetChanged();
          break;
      }
    }
  };
}