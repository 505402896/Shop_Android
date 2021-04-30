package com.example.a50540.lastorder;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class ItemList extends AppCompatActivity {
    private Toolbar toolbar_head;
    private CollapsingToolbarLayout toolbar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        init();

        toolbar_layout.setBackgroundColor(getColor(R.color.white));
        toolbar_layout.setCollapsedTitleTextColor(getColor(R.color.white));

        toolbar_head.setTitle("商品详细");
        toolbar_head.setNavigationIcon(R.drawable.qmui_icon_topbar_back);
        toolbar_head.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar_head);

        toolbar_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void init() {
        toolbar_head = (Toolbar)findViewById(R.id.detail_toolbar);
        toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
    }
}
