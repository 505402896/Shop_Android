package com.example.a50540.lastorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fourth extends Fragment {
  LinearLayout fourth_chat_item;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fourth,container,false);
    fourth_chat_item = (LinearLayout) view.findViewById(R.id.fourth_chat_item);

    fourth_chat_item.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), Chat.class);
        startActivity(intent);
      }
    });
    return view;
  }

}