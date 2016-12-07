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
import java.util.Locale;

public class MyTaskInformationActivity extends AppCompatActivity {

    private static final String TAG = "DevelopmentHelperDemo";

    String mTaskName;

    AVObject mObject;

    private String mTaskId;

    private TextView mTaskNameTextView;
    private TextView mTaskIdTextView;
    private TextView mTaskDescribeTextView;
    private TextView mTaskCreatorTextView;
    private TextView mTaskFinisherTextView;
    private TextView mTaskProjectTextView;
    private TextView mTaskCreateDataTextView;
    private TextView mTaskCloseDataTextView;
    private Button mTaskCloseTaskButton;

    private TextView mChangeTaskName;
    private TextView mChangeTaskDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_information);

        mTaskNameTextView = (TextView) findViewById(R.id.my_task_information_task_name);
        mTaskIdTextView = (TextView) findViewById(R.id.my_task_information_task_id);
        mTaskDescribeTextView = (TextView) findViewById(R.id.my_task_information_task_describe);
        mTaskCreatorTextView = (TextView) findViewById(R.id.my_task_information_task_creator);
        mTaskFinisherTextView = (TextView) findViewById(R.id.my_task_information_task_finisher);
        mTaskProjectTextView = (TextView) findViewById(R.id.my_task_information_task_project);
        mTaskCreateDataTextView = (TextView) findViewById(R.id.my_task_information_task_create_date);
        mTaskCloseDataTextView = (TextView) findViewById(R.id.my_task_information_task_close_date);
        mTaskCloseTaskButton = (Button) findViewById(R.id.my_task_information_close_task);

        mTaskCloseTaskButton.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));

        mChangeTaskName = (TextView) findViewById(R.id.change_task_name);
        mChangeTaskDescribe = (TextView) findViewById(R.id.change_task_describe);

        Intent intent = getIntent();
        mTaskName = intent.getStringExtra("taskName");
        mTaskNameTextView.setText(mTaskName);
        initData();
        changeClickListener();
    }

    private void changeClickListener() {
        mChangeTaskName.setOnClickListener(new TextView.OnClickListener() {
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
                        String objectId = mTaskIdTextView.getText().toString().trim();
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
                                            mTaskNameTextView.setText(newTaskName);
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

        mChangeTaskDescribe.setOnClickListener(new TextView.OnClickListener() {
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
                        String objectId = mTaskIdTextView.getText().toString().trim();
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
                                            mTaskDescribeTextView.setText(newTaskDescribe);
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
        query.whereEqualTo("taskName", mTaskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                mObject = avObject;
                if (e == null) {
                    mTaskId = avObject.getObjectId();
                    mTaskIdTextView.setText(mTaskId);
                    String taskDescribe = avObject.getString("taskDescribe");
                    mTaskDescribeTextView.setText(taskDescribe);
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
                    avObject.fetchInBackground("project", new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (e == null) {
                                AVObject project = avObject.getAVObject("project");
                                String projectName = project.getString("projectName");
                                mTaskProjectTextView.setText(projectName);
                            } else {
                                Log.v(TAG, "ProjectInformationActivity get project fail.error = " + e.getMessage());
                            }
                        }
                    });
                    AVUser currentUser = AVUser.getCurrentUser();
                    mTaskFinisherTextView.setText(currentUser.getUsername());
                    Date createDate = avObject.getCreatedAt();
                    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String createDateText = format.format(createDate);
                    mTaskCreateDataTextView.setText(createDateText);
                    int state = avObject.getInt("state");
                    if (state == 2) {
                        mTaskCloseTaskButton.setVisibility(View.GONE);
                        Date closeDate = avObject.getUpdatedAt();
                        String closeDateText = format.format(closeDate);
                        mTaskCloseDataTextView.setText(closeDateText);
                    } else {
                        mTaskCloseTaskButton.setVisibility(View.VISIBLE);
                        mTaskCloseDataTextView.setText("未关闭");
                        mTaskCloseTaskButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                avObject.put("state", 2);
                                Date closeDate = avObject.getUpdatedAt();
                                String closeDateText = format.format(closeDate);
                                mTaskCloseDataTextView.setText(closeDateText);
                                mTaskCloseTaskButton.setVisibility(View.GONE);
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
