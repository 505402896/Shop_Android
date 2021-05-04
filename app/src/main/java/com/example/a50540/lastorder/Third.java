package com.example.a50540.lastorder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a50540.lastorder.adapter.FirstAdapter;
import com.example.a50540.lastorder.util.Common;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class Third extends Fragment {

    ImageView imageButton;

    EditText m1_title,m1_price,m1_phone,m1_detail;
    QMUIRoundButton third_btn_publish;
    public byte[] image;
    String path = "";
    String fileName = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third,container,false);
        imageButton = (ImageView)view.findViewById(R.id.m1_image);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            }
        });

        m1_title = (EditText)view.findViewById(R.id.m1_title);
        m1_price = (EditText)view.findViewById(R.id.m1_price);
        m1_detail = (EditText)view.findViewById(R.id.m1_detail);
        m1_phone = (EditText)view.findViewById(R.id.m1_phone);





        third_btn_publish = (QMUIRoundButton)view.findViewById(R.id.third_btn_publish);
        third_btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(path);
                fileName = file.getName();

                publishImg(file);
                publishContent(fileName);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            path = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }
    //上传商品详情
    private void publishContent(String fileName) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = Common.SERVER_URL +"/goods/getGoodsInfo";
        //获取存储在内存中的用户id
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        int uid = pref.getInt("uid",0);
        String name = pref.getString("name",null);

        Map<String,Object> map = new HashMap<>();
        map.put("title",m1_title.getText().toString());
        map.put("price",Integer.valueOf(m1_price.getText().toString()));
        // TODO 分类还没做！！
        map.put("type",1);
        map.put("phone",m1_phone.getText().toString());
        map.put("detail",m1_detail.getText().toString());
        map.put("pic",fileName);
        map.put("uid",uid);
        map.put("name",name);

        JSONObject jsonObject = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Toast.makeText(getActivity(),response.body().string(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //    上传图片
    private void publishImg(File file) {
        String url = Common.SERVER_URL +"/goods/getGoodImg";
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");//设置类型，类型为八位字节流

        RequestBody requestBody = RequestBody.create(mediaType, file);//把文件与类型放入请求体
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)//文件名
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBody)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        imageButton.setImageBitmap(bm);
    }

    /**展示图片*/
    private void displayImage(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageButton.setImageBitmap(bitmap);
    }



}
