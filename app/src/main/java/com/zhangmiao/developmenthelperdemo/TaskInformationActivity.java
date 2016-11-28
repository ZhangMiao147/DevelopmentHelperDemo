package com.zhangmiao.developmenthelperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskInformationActivity extends AppCompatActivity {
    private static final String TAG = "DevelopmentHelperDemo";
    private TextView taskIdTextView;
    private TextView taskDescribeTextView;
    private TextView taskCreatorTextView;
    private TextView taskFinisherTextView;
    private TextView taskProjectTextView;
    private TextView taskCreateDataTextView;
    private TextView taskCloseDataTextView;

    private Button receiveTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);

        TextView taskNameTextView = (TextView) findViewById(R.id.task_information_task_name);
        taskIdTextView = (TextView) findViewById(R.id.task_information_task_id);
        taskCreatorTextView = (TextView) findViewById(R.id.task_information_task_creator);
        taskFinisherTextView = (TextView) findViewById(R.id.task_information_task_finisher);
        taskProjectTextView = (TextView) findViewById(R.id.task_information_task_project);
        taskDescribeTextView = (TextView) findViewById(R.id.task_information_task_describe);
        taskCreateDataTextView = (TextView) findViewById(R.id.task_information_task_create_date);
        taskCloseDataTextView = (TextView) findViewById(R.id.task_information_task_close_date);
        receiveTaskButton = (Button) findViewById(R.id.task_information_receive_task);
        Intent intent = getIntent();
        String taskName = intent.getStringExtra("taskName");
        String projectName = intent.getStringExtra("projectName");
        if (taskNameTextView != null) {
            taskNameTextView.setText(taskName);
        }
        if(!projectName.isEmpty()){
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
                                taskCreatorTextView.setText(creatorName);
                            } else {
                                Log.v(TAG, "TaskInformationActivity get creator fail.error = " + e.getMessage());
                            }
                        }
                    });
                    final String[] finisherName = new String[]{""};
                    int state = avObject.getInt("state");
                    if(state == 0){
                        finisherName[0] = "任务未领取";
                        taskFinisherTextView.setText(finisherName[0]);
                        receiveTaskButton.setVisibility(Button.VISIBLE);
                        receiveTaskButton.setOnClickListener(new View.OnClickListener() {
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
                                            receiveTaskButton.setVisibility(Button.GONE);
                                            finisherName[0] = currentUser.getUsername();
                                            taskFinisherTextView.setText(finisherName[0]);
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
                                        taskFinisherTextView.setText(finisherName[0]);
                                    }
                                } else {
                                    Log.v(TAG, "TaskInformationActivity get creator fail.error = " + e.getMessage());
                                }
                            }
                        });
                    }
                    Date createDate = avObject.getCreatedAt();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createDateText = format.format(createDate);
                    final String[] closeDateText = new String[]{"未关闭"};
                    taskIdTextView.setText(taskId);
                    taskDescribeTextView.setText(taskDescribe);
                    taskFinisherTextView.setText(finisherName[0]);
                    taskCloseDataTextView.setText(closeDateText[0]);
                    taskCreateDataTextView.setText(createDateText);
                } else {
                    Log.v(TAG, "TaskInformationActivity getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

}
