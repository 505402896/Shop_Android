package com.example.a50540.lastorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.suke.widget.SwitchButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.kingvcn.kv_wsn.ZigBeeAPI;

import cn.kingvcn.kv_wsn.bean.Relay;
import cn.kingvcn.kv_wsn.response.onAtmosphericPressureResponse;
import cn.kingvcn.kv_wsn.response.onCurtainStateResponse;
import cn.kingvcn.kv_wsn.response.onFWDDSDataResponse;
import cn.kingvcn.kv_wsn.response.onPm25Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Second extends Fragment {
    CardView btn_jum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second,container,false);
        btn_jum = (CardView)view.findViewById(R.id.btn_jump);
        btn_jum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ItemList.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
