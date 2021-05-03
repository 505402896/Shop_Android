package com.example.a50540.lastorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class Fifth extends Fragment {

  TextView tv_username,tv_name;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fifth,container,false);
    tv_username = (TextView)view.findViewById(R.id.fifth_tv_username);
    tv_name = (TextView)view.findViewById(R.id.fifth_tv_name);
    SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
    String username = pref.getString("username",null);
    String name = pref.getString("name",null);
    String info = "账号："+username;
    tv_username.setText(info);
    tv_name.setText(name);
    return view;
  }

}