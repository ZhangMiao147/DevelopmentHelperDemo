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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zhangmiao on 2016/11/24.
 */
public class TaskInformationActivity extends AppCompatActivity {
    private static final String TAG = "DevelopmentHelperDemo";
    private TextView taskNameTextView;
    private TextView taskIdTextView;
    private TextView taskDescribeTextView;
    private TextView taskCreatorTextView;
    private TextView taskFinisherTextView;
    private TextView taskProjectTextView;
    private TextView taskCreateDataTextView;
    private TextView taskCloseDataTextView;

    private Button receiveTaskButton;

    //private Boolean activityFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);

        taskNameTextView = (TextView) findViewById(R.id.task_information_task_name);
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
        final String buttonText = intent.getStringExtra("buttonText");
        receiveTaskButton.setText(buttonText);
        taskNameTextView.setText(taskName);
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

                    avObject.fetchInBackground("finisher", new GetCallback<AVObject>() {
                        @Override
                        public void done(final AVObject avObject, AVException e) {
                            Log.v(TAG,"avObject ID = " + avObject.getObjectId());
                            if (e == null) {
                                AVUser finisher = avObject.getAVUser("finisher");
                                if(finisher != null) {
                                    finisherName[0] = finisher.getUsername();
                                    taskFinisherTextView.setText(finisherName[0]);
                                }
                                if (finisherName[0] == null || finisherName[0].isEmpty()) {
                                    finisherName[0] = "任务未领取";
                                    taskFinisherTextView.setText(finisherName[0]);
                                    receiveTaskButton.setVisibility(Button.VISIBLE);
                                    receiveTaskButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final AVUser currentUser = AVUser.getCurrentUser();
                                            avObject.put("finisher", currentUser);
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
                                }


                            } else {
                                Log.v(TAG, "TaskInformationActivity get creator fail.error = " + e.getMessage());
                            }
                        }
                    });



                    avObject.fetchInBackground("project", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVObject project = avObject.getAVObject("project");
                                String projectName = project.getString("projectName");
                                taskProjectTextView.setText(projectName);
                            } else {
                                Log.v(TAG, "TaskInformationActivity get project fail.error = " + e.getMessage());
                            }
                        }
                    });

                    int state = avObject.getInt("state");
                    Date createDate = avObject.getCreatedAt();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createDateText = format.format(createDate);
                    final String[] closeDateText = new String[]{"未关闭"};
                    switch (state) {
                        case 0:
                        case 1:
                            if(!receiveTaskButton.getText().equals("领取任务")){
                                receiveTaskButton.setVisibility(View.VISIBLE);
                                receiveTaskButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.v(TAG," 完成任务 ");
                                        avObject.put("state", 2);
                                        avObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    Log.v(TAG, "TaskData setState success");
                                                    receiveTaskButton.setVisibility(View.GONE);
                                                    Date closeDate = avObject.getUpdatedAt();
                                                    closeDateText[0] = format.format(closeDate);
                                                    taskCloseDataTextView.setText(closeDateText[0]);
                                                } else {
                                                    Log.v(TAG, "TaskData setState fail.error = " + e.getMessage());
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                            break;
                        case 2:
                        case 3:
                        case 4:
                            Date closeDate = avObject.getUpdatedAt();
                            closeDateText[0] = format.format(closeDate);
                            break;
                        default:
                            break;
                    }
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
