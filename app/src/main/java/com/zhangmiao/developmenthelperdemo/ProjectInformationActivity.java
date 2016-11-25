package com.zhangmiao.developmenthelperdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangmiao on 2016/11/23.
 */
public class ProjectInformationActivity extends AppCompatActivity {
    private static final String TAG = "DevelopmentHelperDemo";
    private TextView projectNameTextView;
    private TextView projectIdTextView;
    private TextView projectDescribeTextView;
    private TextView projectCreatorTextView;
    private TextView projectMemberTextView;
    private TextView projectCreateDataTextView;
    private TextView projectCloseDataTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_information);

        projectNameTextView = (TextView) findViewById(R.id.project_information_project_name);
        projectIdTextView = (TextView) findViewById(R.id.project_information_project_id);
        projectCreatorTextView = (TextView) findViewById(R.id.project_information_project_creator);
        projectMemberTextView = (TextView) findViewById(R.id.project_information_project_member);
        projectDescribeTextView = (TextView) findViewById(R.id.project_information_project_describe);
        projectCreateDataTextView = (TextView) findViewById(R.id.project_information_project_create_date);
        projectCloseDataTextView = (TextView) findViewById(R.id.project_information_project_close_date);
     /*
        */
        Intent intent = getIntent();
        String projectName = intent.getStringExtra("projectName");
        Log.v(TAG, "ProjectInformationActivity projectName = " + projectName);
        projectNameTextView.setText(projectName);
        if (!projectName.isEmpty()) {
            setData(projectName);
        }
    }

    private void setData(final String projectName) {
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName", projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    String projectId = avObject.getObjectId();
                    String projectDescribe = avObject.getString("projectDescribe");

                    avObject.fetchInBackground("creator", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVUser creator = avObject.getAVUser("creator");
                                String creatorName = creator.getUsername();
                                projectCreatorTextView.setText(creatorName);
                            } else {
                                Log.v(TAG, "ProjectInformationActivity get creator fail.error = " + e.getMessage());
                            }
                        }
                    });

                    int state = avObject.getInt("state");
                    Date createDate = avObject.getCreatedAt();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createDateText = format.format(createDate);
                    String closeDateText = "未关闭";
                    switch (state) {
                        case 0:
                            break;
                        case 1:
                        case 2:
                        case 3:
                            Date closeDate = avObject.getUpdatedAt();
                            closeDateText = format.format(closeDate);
                            break;
                        default:
                            break;
                    }

                    avObject.fetchIfNeededInBackground("member", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            ArrayList<AVUser> memberList = (ArrayList<AVUser>) avObject.getList("member");
                            String membersNameText = "";
                            for (AVUser member : memberList) {
                                String memberName = member.getUsername();
                                membersNameText += memberName + " ";
                            }
                            projectMemberTextView.setText(membersNameText);
                        }
                    });

                    projectCloseDataTextView.setText(closeDateText);
                    projectIdTextView.setText(projectId);
                    projectDescribeTextView.setText(projectDescribe);

                    projectCreateDataTextView.setText(createDateText);
                } else {
                    Log.v(TAG, "ProjectInformationActivity getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }


}
