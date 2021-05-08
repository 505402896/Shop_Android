package com.example.a50540.lastorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.a50540.lastorder.adapter.ClassifyAdapter;
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


public class ItemList extends AppCompatActivity {
    private ListView listView;
    private Map<String,Object> map;
    private List<Map<String,Object>> list;
    ClassifyAdapter classifyAdapter;

    private int totalHeight = 0;  //定义总高度
    int type;

    private Toolbar toolbar_head;
    private CollapsingToolbarLayout toolbar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        init();

        listView = (ListView) findViewById(R.id.classify_list);
        classifyAdapter = new ClassifyAdapter(getData(),ItemList.this,R.layout.classify_item);

        listView.setAdapter(classifyAdapter);

        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);

        initData();

        toolbar_layout.setBackgroundColor(getColor(R.color.white));
        toolbar_layout.setCollapsedTitleTextColor(getColor(R.color.white));

        toolbar_head.setTitle("商品详细");
        toolbar_head.setNavigationIcon(R.drawable.qmui_icon_topbar_back);
        toolbar_head.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar_head);

        toolbar_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        String url = Common.SERVER_URL + "/goods/getGoodsByType?type="+type;
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

    public void init() {
        toolbar_head = (Toolbar)findViewById(R.id.detail_toolbar);
        toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
    }

    private List<Map<String,Object>> getData() {
        list = new ArrayList<Map<String, Object>>();
        map = new HashMap<>();
        return list;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    Result result = (Result) bundle.getSerializable("result");
                    JSONArray jsonArray = (JSONArray) result.getData();
                    try {
                        for(int i = 0;i < jsonArray.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("gid",jsonArray.getJSONObject(i).get("gid"));
                            map.put("pic", jsonArray.getJSONObject(i).get("pic"));
                            map.put("title", jsonArray.getJSONObject(i).get("title"));
                            map.put("price", jsonArray.getJSONObject(i).get("price"));
                            list.add(map);
                        }
                    }catch (JSONException e) {
                        Log.d("",e.getMessage());
                    }
                    classifyAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
