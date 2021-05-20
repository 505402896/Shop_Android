package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.a50540.lastorder.GoodDetailActivity;
import com.example.a50540.lastorder.LoginActivity;
import com.example.a50540.lastorder.MainActivity;
import com.example.a50540.lastorder.R;
import com.example.a50540.lastorder.util.AsyncBitmapLoader;
import com.example.a50540.lastorder.util.Common;

import java.util.List;
import java.util.Map;

public class FirstAdapter extends BaseAdapter {
  private AsyncBitmapLoader asyncBitmapLoader;
  List<Map<String,Object>> list;
  Context context;
  int layout;

  public FirstAdapter(List<Map<String, Object>> list, Context context,int layout) {
    this.list = list;
    this.context = context;
    this.layout = layout;
    asyncBitmapLoader=new AsyncBitmapLoader();
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
  public View getView(final int i, View view, ViewGroup viewGroup) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewHolder holder = new ViewHolder();
    if (view == null) {

      view = inflater.inflate(R.layout.first_item,viewGroup,false);
      holder.good = (ImageView)view.findViewById(R.id.first_img_good);
      holder.title = (TextView)view.findViewById(R.id.fitst_tv_name);
      holder.price = (TextView)view.findViewById(R.id.first_tv_price);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
      String imageURL = Common.IMAGE_BASE_PATH + list.get(i).get("pic").toString();
      Bitmap bitmap=asyncBitmapLoader.loadBitmap(holder.good, imageURL, new AsyncBitmapLoader.ImageCallBack() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap) {
          // TODO Auto-generated method stub
          imageView.setImageBitmap(bitmap);
        }
      });
    if(bitmap == null)
    {
      holder.good.setImageResource(R.mipmap.loading);
    }
    else
    {
      holder.good.setImageBitmap(bitmap);
    }
    final int gid = Integer.valueOf(list.get(i).get("gid").toString());
    holder.title.setText(list.get(i).get("title").toString());
    holder.price.setText(list.get(i).get("price").toString());

    holder.good.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        intent.putExtra("gid",gid);
        context.startActivity(intent);
      }
    });
    return view;
  }

  static class ViewHolder {
    public ImageView good;
    public TextView title;
    public TextView price;
  }
}


