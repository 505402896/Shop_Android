package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
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
import com.example.a50540.lastorder.R;
import com.example.a50540.lastorder.util.AsyncBitmapLoader;
import com.example.a50540.lastorder.util.Common;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;
import java.util.Map;

public class ClassifyAdapter extends BaseAdapter {
  List<Map<String,Object>> list;
  Context context;
  int layout;
  ImageLoader imageLoader;

  public ClassifyAdapter(List<Map<String, Object>> list, Context context, int layout) {
    RequestQueue queue = Volley.newRequestQueue(context);
    imageLoader = new ImageLoader(queue,new BitmapCache());
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
  public View getView(int position, View convertView, final ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewHolder holder = new ViewHolder();
    final String imageURL = Common.IMAGE_BASE_PATH + list.get(position).get("pic").toString();
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.classify_item, parent, false);
      holder.img = (NetworkImageView) convertView.findViewById(R.id.classify_img);
      holder.title = (TextView) convertView.findViewById(R.id.classify_title);
      holder.price = (TextView) convertView.findViewById(R.id.classify_price);
      holder.img.setDefaultImageResId(R.mipmap.loading);
      holder.img.setImageUrl(imageURL, imageLoader);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
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
    NetworkImageView img;
    TextView title;
    TextView price;
  }

  public class BitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
      // 获取应用程序最大可用内存
      int maxMemory = (int) Runtime.getRuntime().maxMemory();
      int cacheSize = maxMemory / 8;
      mCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
          return bitmap.getRowBytes() * bitmap.getHeight();
        }
      };
    }

    @Override
    public Bitmap getBitmap(String url) {
      return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
      mCache.put(url, bitmap);
    }

  }
}
