package com.example.a50540.lastorder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a50540.lastorder.R;
import com.example.a50540.lastorder.util.AsyncBitmapLoader;
import com.example.a50540.lastorder.util.Common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyGoodAdpter extends BaseAdapter {
  private AsyncBitmapLoader asyncBitmapLoader;
  List<Map<String,Object>> list;
  Context context;
  int layout;

  public MyGoodAdpter(List<Map<String, Object>> list, Context context, int layout) {
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
  public View getView(final int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ViewHolder holder = new ViewHolder();
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.my_good_item, parent, false);
      holder.img = (AppCompatImageView) convertView.findViewById(R.id.my_good_img);
      holder.title = (TextView) convertView.findViewById(R.id.my_good_title);
      holder.price = (TextView) convertView.findViewById(R.id.my_good_price);
      holder.btn_del = (ImageButton) convertView.findViewById(R.id.my_good_btn_del);
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
    holder.btn_del.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = Common.SERVER_URL + "/goods/delMyGoods/"+gid;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            Log.d("tag",e.getMessage());
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {

          }
        });
        list.remove(position);
        notifyDataSetChanged();
      }
    });
    return convertView;
  }

  static class ViewHolder {
    AppCompatImageView img;
    TextView title;
    TextView price;
    ImageButton btn_del;
  }
}
