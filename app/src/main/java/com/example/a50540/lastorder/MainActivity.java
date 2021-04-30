package com.example.a50540.lastorder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.kingvcn.kv_wsn.ZigBeeAPI;
import cn.kingvcn.kv_wsn.bean.Relay;
import cn.kingvcn.kv_wsn.response.onRelayResponse;
import cn.kingvcn.kv_wsn.socket.onSocketLinkListener;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class MainActivity extends AppCompatActivity {

//    static final String ip = "http://192.168.31.1:8081/api";
static final String ip = "http://192.168.31.1:8080";
    static String user = null;
    private ViewPager myViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> myListFragmentPager;   //存放多个fragment
    private BlurView blurView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    myViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_sort:
                    myViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_publish:
                    myViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_message:
                    myViewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation_my:
                    myViewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myViewPager = (ViewPager) findViewById(R.id.vp_pager);

        myListFragmentPager = new ArrayList<>();
        myListFragmentPager.add(new First());
        myListFragmentPager.add(new Second());
        myListFragmentPager.add(new Third());
        myListFragmentPager.add(new Fourth());
        myListFragmentPager.add(new Fifth());

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), myListFragmentPager);


        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        myViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_sort);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_publish);
                        break;
                    case 3:
                        navigation.setSelectedItemId(R.id.navigation_message);
                        break;
                    case 4:
                        navigation.setSelectedItemId(R.id.navigation_my);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        myViewPager.setAdapter(myFragmentPagerAdapter);

        user = getIntent().getStringExtra("username");

        blurView = (BlurView) findViewById(R.id.blurView);
        final int radius = 16;
        final View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
