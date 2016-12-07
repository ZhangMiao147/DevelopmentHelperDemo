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
import java.util.Date;
import java.util.Locale;

public class TaskInformationActivity extends AppCompatActivity {
    private static final String TAG = "DevelopmentHelperDemo";
    private TextView mTaskIdTextView;
    private TextView mTaskDescribeTextView;
    private TextView mTaskCreatorTextView;
    private TextView mTaskFinisherTextView;
    private TextView mTaskCreateDataTextView;
    private TextView mTaskCloseDataTextView;

    private Button mReceiveTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);

        TextView taskNameTextView = (TextView) findViewById(R.id.task_information_task_name);
        mTaskIdTextView = (TextView) findViewById(R.id.task_information_task_id);
        mTaskCreatorTextView = (TextView) findViewById(R.id.task_information_task_creator);
        mTaskFinisherTextView = (TextView) findViewById(R.id.task_information_task_finisher);
        TextView taskProjectTextView = (TextView) findViewById(R.id.task_information_task_project);
        mTaskDescribeTextView = (TextView) findViewById(R.id.task_information_task_describe);
        mTaskCreateDataTextView = (TextView) findViewById(R.id.task_information_task_create_date);
        mTaskCloseDataTextView = (TextView) findViewById(R.id.task_information_task_close_date);
        mReceiveTaskButton = (Button) findViewById(R.id.task_information_receive_task);
        mReceiveTaskButton.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));

        Intent intent = getIntent();
        String taskName = intent.getStringExtra("taskName");
        String projectName = intent.getStringExtra("projectName");
        if (taskNameTextView != null) {
            taskNameTextView.setText(taskName);
        }
        if(!projectName.isEmpty() && taskProjectTextView != null){
            taskProjectTextView.setText(projectName);
        }
        if (!taskName.isEmpty()) {
            setData(taskName);
        }
    }

    private void setData(final String taskName) {
        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        query.whereEqualTo("taskName", taskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null) {
                    String taskId = avObject.getObjectId();
                    String taskDescribe = avObject.getString("taskDescribe");

                    avObject.fetchInBackground("creator", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVUser creator = avObject.getAVUser("creator");
                                String creatorName = creator.getUsername();
                                mTaskCreatorTextView.setText(creatorName);
                            } else {
                                Log.v(TAG, "TaskInformationActivity get creator fail.error = " + e.getMessage());
                            }
                        }
                    });
                    final String[] finisherName = new String[]{""};
                    int state = avObject.getInt("state");
                    if(state == 0){
                        finisherName[0] = "任务未领取";
                        mTaskFinisherTextView.setText(finisherName[0]);
                        mReceiveTaskButton.setVisibility(Button.VISIBLE);
                        mReceiveTaskButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AVUser currentUser = AVUser.getCurrentUser();
                                avObject.put("finisher", currentUser);
                                avObject.put("finisherName", currentUser.getUsername());
                                avObject.put("state",1);
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            Log.v(TAG, "addFinisher success");
                                            mReceiveTaskButton.setVisibility(Button.GONE);
                                            finisherName[0] = currentUser.getUsername();
                                            mTaskFinisherTextView.setText(finisherName[0]);
                                        } else {
                                            Log.v(TAG, "addFinisher fail.error = " + e.getMessage());
                                        }
                                    }
                                });
                            }
                        });
                    }else {
                        avObject.fetchInBackground("finisher", new GetCallback<AVObject>() {
                            @Override
                            public void done(final AVObject avObject, AVException e) {
                                if (e == null) {
                                    AVUser finisher = avObject.getAVUser("finisher");
                                    if (finisher != null) {
                                        finisherName[0] = finisher.getUsername();
                                        mTaskFinisherTextView.setText(finisherName[0]);
                                    }
                                } else {
                                    Log.v(TAG, "TaskInformationActivity get creator fail.error = " + e.getMessage());
                                }
                            }
                        });
                    }
                    Date createDate = avObject.getCreatedAt();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String createDateText = format.format(createDate);
                    final String[] closeDateText = new String[]{"未关闭"};
                    mTaskIdTextView.setText(taskId);
                    mTaskDescribeTextView.setText(taskDescribe);
                    mTaskFinisherTextView.setText(finisherName[0]);
                    mTaskCloseDataTextView.setText(closeDateText[0]);
                    mTaskCreateDataTextView.setText(createDateText);
                } else {
                    Log.v(TAG, "TaskInformationActivity getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

}
