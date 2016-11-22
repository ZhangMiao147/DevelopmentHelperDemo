package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangmiao on 2016/11/18.
 */
public class SignInFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_in,container,false);
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
