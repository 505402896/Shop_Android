package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.Chat;
import com.example.a50540.lastorder.GoodDetailActivity;
import com.example.a50540.lastorder.R;

import java.util.List;
import java.util.Map;

public class RecentAdapter extends BaseAdapter{
    List<Map<String,Object>> list;
    Context context;
    int layout;

    public RecentAdapter(List<Map<String, Object>> list, Context context, int layout) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
          convertView = inflater.inflate(R.layout.recent_list,parent,false);
          holder.name = convertView.findViewById(R.id.fourth_recent_name);
          holder.content = convertView.findViewById(R.id.fourth_recent_content);
          holder.time = convertView.findViewById(R.id.fourth_recent_time);
          convertView.setTag(holder);
        } else {
          holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.content.setText(list.get(position).get("content").toString());
        holder.time.setText(list.get(position).get("time").toString());

        holder.name.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent toChat = new Intent(context, Chat.class);
//          要聊天人的id
            toChat.putExtra("uid", Integer.valueOf(list.get(position).get("targetId").toString()));
            toChat.putExtra("name",list.get(position).get("name").toString());
            context.startActivity(toChat);
          }
        });

        holder.content.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent toChat = new Intent(context, Chat.class);
//          要聊天人的id
            toChat.putExtra("uid", Integer.valueOf(list.get(position).get("targetId").toString()));
            toChat.putExtra("name",list.get(position).get("name").toString());
            context.startActivity(toChat);
          }
        });

        holder.time.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent toChat = new Intent(context, Chat.class);
//          要聊天人的id
            toChat.putExtra("uid", Integer.valueOf(list.get(position).get("targetId").toString()));
            toChat.putExtra("name",list.get(position).get("name").toString());
            context.startActivity(toChat);
          }
        });

      return convertView;
    }

  static class ViewHolder{
      public TextView name;
      public TextView content;
      public TextView time;
    }
}
