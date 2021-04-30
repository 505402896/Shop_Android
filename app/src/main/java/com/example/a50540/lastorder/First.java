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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kingvcn.kv_wsn.ZigBeeAPI;
import cn.kingvcn.kv_wsn.response.onAtmosphericPressureResponse;
import cn.kingvcn.kv_wsn.response.onFWDDSDataResponse;
import cn.kingvcn.kv_wsn.response.onFlameStateResponse;
import cn.kingvcn.kv_wsn.response.onHumitureResponse;
import cn.kingvcn.kv_wsn.response.onInfraredStateResponse;
import cn.kingvcn.kv_wsn.response.onPhotoresistanceResponse;
import cn.kingvcn.kv_wsn.response.onPm25Response;
import cn.kingvcn.kv_wsn.response.onSmokeStateResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class First extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first,container,false);


        return view;
    }



//    public void send(){
//        Map map = new HashMap();
//        map.put("pressure",data.getPressure());
//        JSONObject jsonObject = new JSONObject(map);
//        RequestBody requestBody = RequestBody.create(null,jsonObject.toString());
//        final Request request = new Request.Builder().url(MainActivity.ip+"saveServlet").post(requestBody).build();
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("error",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });
//    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 2:
//                    tv_temp.setText(msg.obj+"℃");
//                    data.setTemp(msg.obj.toString());
//                    break;
//
//            }
//        }
//    };
}
