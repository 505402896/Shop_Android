package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a50540.lastorder.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends BaseAdapter {
  List<Map<String,Object>> list;
  Context context;
  int layout;

  public CommentAdapter(List<Map<String, Object>> list, Context context, int layout) {
    this.list = list;
    this.context = context;
    this.layout = layout;
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewHolder holder = new ViewHolder();

//    绑定模板和每个控件
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.comment_item,parent,false);
      holder.name = convertView.findViewById(R.id.comment_name);
      holder.content = convertView.findViewById(R.id.comment_content);
      holder.time = convertView.findViewById(R.id.comment_time);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

//    给每个要展示数据的控件绑定数据   由定义适配器的时候 通过getData()方法传入数据
    holder.name.setText(list.get(position).get("name").toString());
    holder.content.setText(list.get(position).get("content").toString());
    holder.time.setText(list.get(position).get("time").toString().substring(5));

    return convertView;
  }

//  定义一个View容器 定义了包括你想要往里塞数据的所有控件
  static class ViewHolder {
    TextView name;
    TextView time;
    TextView content;
  }
}
