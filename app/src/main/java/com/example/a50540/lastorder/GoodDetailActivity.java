package com.example.a50540.lastorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class GoodDetailActivity extends AppCompatActivity {
  ImageView btn_return;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_detail);

    btn_return = (ImageView)findViewById(R.id.detail_btn_return1);

    btn_return.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }
}
