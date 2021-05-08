package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.GoodDetailActivity;
import com.example.a50540.lastorder.R;
import com.example.a50540.lastorder.util.AsyncBitmapLoader;
import com.example.a50540.lastorder.util.Common;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;
import java.util.Map;

public class ClassifyAdapter extends BaseAdapter {
  private AsyncBitmapLoader asyncBitmapLoader;
  List<Map<String,Object>> list;
  Context context;
  int layout;

  public ClassifyAdapter(List<Map<String, Object>> list, Context context, int layout) {
    asyncBitmapLoader=new AsyncBitmapLoader();
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
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.classify_item, parent, false);
      holder.img = (AppCompatImageView) convertView.findViewById(R.id.classify_img);
      holder.title = (TextView) convertView.findViewById(R.id.classify_title);
      holder.price = (TextView) convertView.findViewById(R.id.classify_price);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    String imageURL = Common.IMAGE_BASE_PATH + list.get(position).get("pic").toString();
    Bitmap bitmap=asyncBitmapLoader.loadBitmap(holder.img, imageURL, new AsyncBitmapLoader.ImageCallBack() {
      @Override
      public void imageLoad(ImageView imageView, Bitmap bitmap) {
        // TODO Auto-generated method stub
        imageView.setImageBitmap(bitmap);
      }
    });
    if(bitmap == null)
    {
      holder.img.setImageResource(R.mipmap.loading);
    }
    else
    {
      holder.img.setImageBitmap(bitmap);
    }
    final int gid = Integer.valueOf(list.get(position).get("gid").toString());
    holder.title.setText(list.get(position).get("title").toString());
    holder.price.setText(list.get(position).get("price").toString());

    holder.img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("gid",gid);
        context.startActivity(intent);
      }
    });

    holder.title.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("gid",gid);
        context.startActivity(intent);
      }
    });

    holder.price.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("gid",gid);
        context.startActivity(intent);
      }
    });
    return convertView;
  }

  static class ViewHolder {
    AppCompatImageView img;
    TextView title;
    TextView price;
  }
}
