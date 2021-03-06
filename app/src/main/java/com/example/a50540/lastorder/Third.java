package com.example.a50540.lastorder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButtonDrawable;

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
    QMUIRoundButton btn_study,btn_live,btn_PE,btn_tech,btn_food,btn_make;
    EditText m1_title,m1_price,m1_phone,m1_detail;
    QMUIRoundButton third_btn_publish;
    public byte[] image;
    int classify = 0;   // 1:???????????? 2:???????????? 3:???????????? 4:???????????? 5:???????????? 6:????????????
    String path = "";
    String fileName = "";
    List<QMUIRoundButton> list = new ArrayList<>();
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
                    //??????????????????
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
        btn_food = (QMUIRoundButton)view.findViewById(R.id.third_btn_spfl);
        btn_live = (QMUIRoundButton)view.findViewById(R.id.third_btn_shyp);
        btn_make = (QMUIRoundButton)view.findViewById(R.id.third_btn_hfyp);
        btn_tech = (QMUIRoundButton)view.findViewById(R.id.third_btn_dzsb);
        btn_PE = (QMUIRoundButton)view.findViewById(R.id.third_btn_tyyp);
        btn_study = (QMUIRoundButton)view.findViewById(R.id.third_btn_xxyp);

        list.add(btn_study);
        list.add(btn_live);
        list.add(btn_PE);
        list.add(btn_tech);
        list.add(btn_food);
        list.add(btn_make);

        btn_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 1;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_study);
            }
        });

        btn_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 2;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_live);
            }
        });

        btn_PE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 3;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_PE);
            }
        });

        btn_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 4;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_tech);
            }
        });

        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 5;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_food);
            }
        });

        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify = 6;
                for(QMUIRoundButton button : list) {
                    changeWhite(button);
                }
                changeBlue(btn_make);
            }
        });

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
        //??????????????????
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
    //??????????????????
    private void publishContent(String fileName) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = Common.SERVER_URL +"/goods/getGoodsInfo";
        //?????????????????????????????????id
        SharedPreferences pref = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        int uid = pref.getInt("uid",0);
        String name = pref.getString("name",null);

        final Map<String,Object> map = new HashMap<>();
        map.put("title",m1_title.getText().toString());
        map.put("price",Integer.valueOf(m1_price.getText().toString()));
        // TODO ?????????????????????
        map.put("type",classify);
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
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }
    //    ????????????
    private void publishImg(File file) {
        String url = Common.SERVER_URL +"/goods/getGoodImg";
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");//???????????????????????????????????????

        RequestBody requestBody = RequestBody.create(mediaType, file);//?????????????????????????????????
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody)//?????????
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

    //????????????
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        imageButton.setImageBitmap(bm);
    }

    /**????????????*/
    private void displayImage(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageButton.setImageBitmap(bitmap);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity(),"????????????",Toast.LENGTH_SHORT).show();
                    m1_detail.setText("");
                    m1_phone.setText("");
                    m1_price.setText("");
                    m1_title.setText("");
                    break;
            }
        }
    };

    public void changeBlue(QMUIRoundButton button) {
        QMUIRoundButtonDrawable qmuiRoundButtonDrawable = (QMUIRoundButtonDrawable) button.getBackground();
        ColorStateList colorStateList=ColorStateList.valueOf(getContext().getResources().getColor(R.color.blue));
        qmuiRoundButtonDrawable.setBgData(colorStateList);
        button.setTextColor(Color.WHITE);
    }

    public void changeWhite(QMUIRoundButton button) {
        QMUIRoundButtonDrawable qmuiRoundButtonDrawable = (QMUIRoundButtonDrawable) button.getBackground();
        ColorStateList colorStateList=ColorStateList.valueOf(getContext().getResources().getColor(R.color.white));
        qmuiRoundButtonDrawable.setBgData(colorStateList);
        button.setTextColor(Color.rgb(30,111,255));
    }

}
