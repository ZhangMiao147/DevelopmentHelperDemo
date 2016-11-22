package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiao on 2016/11/18.
 */
public class ExistingTasksFragment extends Fragment {

    private static final String TAG="DevelopmentHelperDemo";

    private Spinner projectSpinner;
    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_existing_tasks,container,false);

        button = (Button)view.findViewById(R.id.add);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





            }
        });

        projectSpinner = (Spinner)view.findViewById(R.id.spinner_project_list);
        list = new ArrayList<>();
        list.add("FirstProject");
        list.add("SecondProject");
        arrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(arrayAdapter);
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, " onItemSelected ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}
