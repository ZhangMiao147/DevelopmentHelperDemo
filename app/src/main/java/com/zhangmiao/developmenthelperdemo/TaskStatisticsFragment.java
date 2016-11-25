package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

/**
 * Created by zhangmiao on 2016/11/18.
 */
public class TaskStatisticsFragment extends Fragment {

    private TextView projectCreator;
    private static final String TAG="DevelopmentHelperDemo";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_statistics,container,false);
        projectCreator = (TextView)view.findViewById(R.id.task_statistics_project_creator);

        String projectName = "FirstProject";
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName",projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null) {
                    Log.v(TAG, "getAVObject success");
                    avObject.fetchInBackground("creator", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVUser user = avObject.getAVUser("creator");
                                String userName = user.getUsername();
                                projectCreator.setText(userName);
                            } else {
                                Log.v(TAG, "TaskInformationActivity get project fail.error = " + e.getMessage());
                            }
                        }
                    });

                }else {
                    Log.v(TAG,"getAVObject fail.error = " + e.getMessage());
                }
            }
        });



        return view;
    }
}
