package com.example.a50540.lastorder;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.a50540.lastorder.adapter.FirstAdapter;
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

import static android.widget.Toast.LENGTH_LONG;

public class First extends Fragment {
    GridView gridView;
    Map<String,Object> map;
    List<Map<String,Object>> list;
    FirstAdapter firstAdapter;
    EditText first_et_search;
    Button first_btn_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first,container,false);

        gridView = (GridView) view.findViewById(R.id.first_grid);

        firstAdapter = new FirstAdapter(getData(),getActivity(),R.layout.first_item);
        gridView.setAdapter(firstAdapter);

        first_btn_search = (Button)view.findViewById(R.id.first_btn_search);
        first_et_search = (EditText)view.findViewById(R.id.first_et_search);

        initData();

        first_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<Map<String,Object>> searchList = new ArrayList<>();
                for(Map<String,Object> map : list) {
                    if(first_et_search.getText().toString().equals(map.get("title"))) {
                        // 当搜索结果不为空时  返回搜索数据
                        list.clear();
                        list.add(map);
                    } else {
                        Toast.makeText(getActivity(),"暂无搜索结果",Toast.LENGTH_SHORT).show();
                    }
                }
                firstAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private List<Map<String,Object>> getData() {
        list = new ArrayList<Map<String, Object>>();
        map = new HashMap<>();
        return list;
    }

    private void initData() {
        String url = Common.SERVER_URL + "/goods/getAllGoods";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fail",e.getMessage());
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
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    Result result = (Result) bundle.getSerializable("result");
                    JSONArray jsonArray = (JSONArray) result.getData();
                    try {
                        for(int i = 0;i < jsonArray.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("pic", jsonArray.getJSONObject(i).get("pic"));
                            map.put("title", jsonArray.getJSONObject(i).get("title"));
                            map.put("price", jsonArray.getJSONObject(i).get("price"));
                            list.add(map);
                            System.out.println(list);
                        }
                    }catch (JSONException e) {
                        Log.d("",e.getMessage());
                    }
                    firstAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
