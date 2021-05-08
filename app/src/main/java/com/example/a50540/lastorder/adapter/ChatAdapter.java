package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a50540.lastorder.R;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;

import java.util.List;
import java.util.Map;

public class ChatAdapter extends BaseAdapter {
  List<Map<String,Object>> list;
  Context context;
  int layout;

  public ChatAdapter(List<Map<String, Object>> list, Context context,int layout) {
    this.list = list;
    this.context = context;
    this.layout = layout;
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int i) {
    return list.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewHolder holder = new ViewHolder();
    if (view == null) {
      view = inflater.inflate(R.layout.chat_list, viewGroup, false);
      holder.head = (QMUIRadiusImageView) view.findViewById(R.id.chat_list_head_icon);
      holder.msg = (QMUIRoundButton) view.findViewById(R.id.chat_list_msg);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    if(list.get(i).get("position").equals("left")) {
      RelativeLayout.LayoutParams head_left = (RelativeLayout.LayoutParams) holder.head.getLayoutParams();
      head_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      holder.head.setImageResource(R.drawable.head_icon);
      RelativeLayout.LayoutParams msg_left = (RelativeLayout.LayoutParams) holder.msg.getLayoutParams();
      msg_left.addRule(RelativeLayout.RIGHT_OF,R.id.chat_list_head_icon);
    } else {
      RelativeLayout.LayoutParams head_right = (RelativeLayout.LayoutParams) holder.head.getLayoutParams();
      head_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      holder.head.setImageResource(R.drawable.genshin);
      RelativeLayout.LayoutParams msg_right = (RelativeLayout.LayoutParams) holder.msg.getLayoutParams();
      msg_right.addRule(RelativeLayout.LEFT_OF,R.id.chat_list_head_icon);
      QMUIRoundButtonDrawable qmuiRoundButtonDrawable = (QMUIRoundButtonDrawable) holder.msg.getBackground();
      ColorStateList colorStateList=ColorStateList.valueOf(context.getResources().getColor(R.color.blue));
      qmuiRoundButtonDrawable.setBgData(colorStateList);
      holder.msg.setTextColor(Color.WHITE);
//      holder.msg.setTextColor(Color.argb(1,30,111,255));
    }
    if(holder.head != null) {
//      holder.head.setImageResource(Integer.valueOf(list.get(i).get("image")+""));
      holder.msg.setText(list.get(i).get("content").toString());
    }
    return view;
  }

  static class ViewHolder {
    QMUIRadiusImageView head;
    QMUIRoundButton msg;
  }
}
