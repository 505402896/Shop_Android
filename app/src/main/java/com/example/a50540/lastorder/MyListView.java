package com.example.a50540.lastorder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {
  public MyListView(Context context) {
    super(context);
  }

  public MyListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }
  /**

   * 这个是用来避免listview只能显示一条数据的

   * @param widthMeasureSpec

   * @param heightMeasureSpec

   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    //测量的大小由一个32位的数字表示，前两位表示测量模式，后30位表示大小，这里需要右移两位才能拿到测量的大小
    int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, heightSpec);
  }
}
