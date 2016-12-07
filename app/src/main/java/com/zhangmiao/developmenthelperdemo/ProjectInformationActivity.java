package com.zhangmiao.developmenthelperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.meizu.common.util.GradientDrawableFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProjectInformationActivity extends AppCompatActivity {
    private static final String TAG = "DevelopmentHelperDemo";
    private TextView mProjectIdTextView;
    private TextView mProjectDescribeTextView;
    private TextView mProjectCreatorTextView;
    private TextView mProjectMemberTextView;
    private TextView mProjectCreateDataTextView;
    private TextView mProjectCloseDataTextView;
    private Button mCloseProjectButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_information);

        TextView projectNameTextView = (TextView) findViewById(R.id.project_information_project_name);
        mProjectIdTextView = (TextView) findViewById(R.id.project_information_project_id);
        mProjectCreatorTextView = (TextView) findViewById(R.id.project_information_project_creator);
        mProjectMemberTextView = (TextView) findViewById(R.id.project_information_project_member);
        mProjectDescribeTextView = (TextView) findViewById(R.id.project_information_project_describe);
        mProjectCreateDataTextView = (TextView) findViewById(R.id.project_information_project_create_date);
        mProjectCloseDataTextView = (TextView) findViewById(R.id.project_information_project_close_date);
        mCloseProjectButton = (Button)findViewById(R.id.project_information_project_close_button);
        mCloseProjectButton.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));

        Intent intent = getIntent();
        String projectName = intent.getStringExtra("projectName");
        Log.v(TAG, "ProjectInformationActivity projectName = " + projectName);
        if(projectNameTextView != null) {
            projectNameTextView.setText(projectName);
        }
        if (!projectName.isEmpty()) {
            setData(projectName);
        }
    }

    private void setData(final String projectName) {
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName", projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    String projectId = avObject.getObjectId();
                    String projectDescribe = avObject.getString("projectDescribe");

                    avObject.fetchInBackground("creator", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVUser creator = avObject.getAVUser("creator");
                                String creatorName = creator.getUsername();
                                mProjectCreatorTextView.setText(creatorName);
                            } else {
                                Log.v(TAG, "ProjectInformationActivity get creator fail.error = " + e.getMessage());
                            }
                        }
                    });

                    int state = avObject.getInt("state");
                    Date createDate = avObject.getCreatedAt();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String createDateText = format.format(createDate);
                    String closeDateText = "未关闭";
                    switch (state) {
                        case 0:
                            mCloseProjectButton.setVisibility(View.VISIBLE);
                            mCloseProjectButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    avObject.put("state", 1);
                                    avObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                Log.v(TAG, "setState success");
                                                mCloseProjectButton.setVisibility(View.GONE);
                                                Date closeDate = avObject.getUpdatedAt();
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                                String closeDateText = format.format(closeDate);
                                                mProjectCloseDataTextView.setText(closeDateText);
                                            } else {
                                                Log.v(TAG, "setState fail.error = " + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        case 1:
                            mCloseProjectButton.setVisibility(View.GONE);
                            Date closeDate = avObject.getUpdatedAt();
                            closeDateText = format.format(closeDate);
                            break;
                        default:
                            break;
                    }

                    avObject.fetchIfNeededInBackground("member", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            @SuppressWarnings("unchecked")
                            ArrayList<AVUser> memberList = (ArrayList<AVUser>) avObject.getList("member");
                            String membersNameText = "";
                            for (AVUser member : memberList) {
                                String memberName = member.getUsername();
                                membersNameText += memberName + ",";
                            }
                            mProjectMemberTextView.setText(membersNameText);
                        }
                    });

                    mProjectCloseDataTextView.setText(closeDateText);
                    mProjectIdTextView.setText(projectId);
                    mProjectDescribeTextView.setText(projectDescribe);
                    mProjectCreateDataTextView.setText(createDateText);
                } else {
                    Log.v(TAG, "ProjectInformationActivity getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }


}
