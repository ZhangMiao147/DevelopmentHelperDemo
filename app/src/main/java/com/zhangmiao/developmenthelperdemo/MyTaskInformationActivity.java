package com.zhangmiao.developmenthelperdemo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MyTaskInformationActivity extends AppCompatActivity {

    private static final String TAG = "DevelopmentHelperDemo";

    String taskName;

    AVObject mObject;

    private String taskId;

    private TextView taskNameTextView;
    private TextView taskIdTextView;
    private TextView taskDescribeTextView;
    private TextView taskCreatorTextView;
    private TextView taskFinisherTextView;
    private TextView taskProjectTextView;
    private TextView taskCreateDataTextView;
    private TextView taskCloseDataTextView;
    private Button taskCloseTaskButton;

    private TextView changeTaskName;
    private TextView changeTaskDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_information);

        taskNameTextView = (TextView) findViewById(R.id.my_task_information_task_name);
        taskIdTextView = (TextView) findViewById(R.id.my_task_information_task_id);
        taskDescribeTextView = (TextView) findViewById(R.id.my_task_information_task_describe);
        taskCreatorTextView = (TextView) findViewById(R.id.my_task_information_task_creator);
        taskFinisherTextView = (TextView) findViewById(R.id.my_task_information_task_finisher);
        taskProjectTextView = (TextView) findViewById(R.id.my_task_information_task_project);
        taskCreateDataTextView = (TextView) findViewById(R.id.my_task_information_task_create_date);
        taskCloseDataTextView = (TextView) findViewById(R.id.my_task_information_task_close_date);
        taskCloseTaskButton = (Button) findViewById(R.id.my_task_information_close_task);

        taskCloseTaskButton.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));

        changeTaskName = (TextView) findViewById(R.id.change_task_name);
        changeTaskDescribe = (TextView) findViewById(R.id.change_task_describe);

        Intent intent = getIntent();
        taskName = intent.getStringExtra("taskName");
        taskNameTextView.setText(taskName);
        initData();
        changeClickListener();
    }

    private void changeClickListener() {
        changeTaskName.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MyTaskInformationActivity.this);
                dialog.setContentView(R.layout.change_text_view_alert_dialog);
                dialog.setCancelable(false);
                View vv = dialog.getWindow().getDecorView();
                Button cancel = (Button) vv.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText editText = (EditText) vv.findViewById(R.id.change_edit_text);
                Button sure = (Button) vv.findViewById(R.id.sure);
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newTaskName = editText.getText().toString().trim();
                        String objectId = taskIdTextView.getText().toString().trim();
                        AVObject avObject = AVObject.createWithoutData("TaskData", objectId);
                        avObject.fetchInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                avObject.put("taskName", newTaskName);
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            CustomAlertDialog customAlertDialog = new CustomAlertDialog(MyTaskInformationActivity.this);
                                            customAlertDialog.setSuccessAlertDialog();
                                            dialog.dismiss();
                                            taskNameTextView.setText(newTaskName);
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
                dialog.show();
            }
        });

        changeTaskDescribe.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MyTaskInformationActivity.this);
                dialog.setContentView(R.layout.change_text_view_alert_dialog);
                dialog.setCancelable(false);
                View vv = dialog.getWindow().getDecorView();
                Button cancel = (Button) vv.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText editText = (EditText) vv.findViewById(R.id.change_edit_text);
                Button sure = (Button) vv.findViewById(R.id.sure);
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newTaskDescribe = editText.getText().toString().trim();
                        String objectId = taskIdTextView.getText().toString().trim();
                        AVObject avObject = AVObject.createWithoutData("TaskData", objectId);
                        avObject.fetchInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                avObject.put("taskDescribe", newTaskDescribe);
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            CustomAlertDialog customAlertDialog = new CustomAlertDialog(MyTaskInformationActivity.this);
                                            customAlertDialog.setSuccessAlertDialog();
                                            dialog.dismiss();
                                            taskDescribeTextView.setText(newTaskDescribe);
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
                dialog.show();
            }
        });

    }

    private void initData() {
        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        query.whereEqualTo("taskName", taskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                mObject = avObject;
                if (e == null) {
                    taskId = avObject.getObjectId();
                    taskIdTextView.setText(taskId);
                    String taskDescribe = avObject.getString("taskDescribe");
                    taskDescribeTextView.setText(taskDescribe);
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
                    avObject.fetchInBackground("project", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVObject project = avObject.getAVObject("project");
                                String projectName = project.getString("projectName");
                                taskProjectTextView.setText(projectName);
                            } else {
                                Log.v(TAG, "ProjectInformationActivity get project fail.error = " + e.getMessage());
                            }
                        }
                    });
                    AVUser currentUser = AVUser.getCurrentUser();
                    taskFinisherTextView.setText(currentUser.getUsername());
                    Date createDate = avObject.getCreatedAt();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createDateText = format.format(createDate);
                    taskCreateDataTextView.setText(createDateText);
                    int state = avObject.getInt("state");
                    if (state == 2) {
                        taskCloseTaskButton.setVisibility(View.GONE);
                        Date closeDate = avObject.getUpdatedAt();
                        String closeDateText = format.format(closeDate);
                        taskCloseDataTextView.setText(closeDateText);
                    } else {
                        taskCloseTaskButton.setVisibility(View.VISIBLE);
                        taskCloseDataTextView.setText("未关闭");
                        taskCloseTaskButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                avObject.put("state", 2);
                                Date closeDate = avObject.getUpdatedAt();
                                String closeDateText = format.format(closeDate);
                                taskCloseDataTextView.setText(closeDateText);
                                taskCloseTaskButton.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    Log.v(TAG, "TaskInformationActivity getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }
}
