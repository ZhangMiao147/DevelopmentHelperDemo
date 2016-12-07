package com.zhangmiao.developmenthelperdemo;

import android.content.Context;
import android.util.AttributeSet;

import flyme.support.v7.widget.LinearLayoutManager;
import flyme.support.v7.widget.RecyclerView;


/**
 * Created by zhangmiao on 2016/12/7.
 */
public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context){
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context,int orientation,boolean reverseLayout){
        super(context,orientation,reverseLayout);
    }

    public WrapContentLinearLayoutManager(Context context,AttributeSet attrs, int defStyleAttr,int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}