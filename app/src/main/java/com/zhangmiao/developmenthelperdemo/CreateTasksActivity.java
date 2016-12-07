package com.zhangmiao.developmenthelperdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.meizu.common.util.GradientDrawableFactory;

import java.util.ArrayList;
import java.util.List;

import flyme.support.v7.app.AlertDialog;

public class CreateTasksActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DevelopmentHelperDemo";

    private EditText projectName;
    private EditText projectDescribe;

    private EditText taskName;
    private EditText taskDescribe;
    private RelativeLayout mChoiceLayout;

    private TextView mChoiceProjectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tasks);
        mChoiceLayout = (RelativeLayout) findViewById(R.id.choice_project);
        mChoiceProjectName = (TextView) findViewById(R.id.choice_project_name);

        mChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCreateProjectNameList();
            }
        });
        projectName = (EditText) findViewById(R.id.et_project_name);
        projectDescribe = (EditText) findViewById(R.id.et_project_describe);


        taskName = (EditText) findViewById(R.id.et_task_name);
        taskDescribe = (EditText) findViewById(R.id.et_task_describe);

        Button createProject = (Button) findViewById(R.id.bt_create_project);
        Button createTask = (Button) findViewById(R.id.bt_create_task);
        createTask.setOnClickListener(this);
        createProject.setOnClickListener(this);

        createTask.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));
        createProject.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));
    }


    public void initCreateProjectNameList()
    {
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.v(TAG, "getProjectList success");
                    final String[] projectNameList = new String[list.size()];
                    int i = 0;
                    for (AVObject object : list) {
                        String projectName = object.getString("projectName");
                        projectNameList[i++] = projectName;
                    }
                    new AlertDialog.Builder(CreateTasksActivity.this, R.style.Theme_Flyme_AppCompat_Light_Dialog_Alert)
                            .setTitle(R.string.handle_select_title)
                            .setItems(projectNameList, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mChoiceProjectName.setText(projectNameList[which]);
                                }
                            }, false)
                            .show();
                } else {
                    Log.v(TAG, "getProjectList fail.error = " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_create_project:
                final String projectNameText = projectName.getText().toString();
                String projectDescribeText = projectDescribe.getText().toString();
                AVUser currentUser = AVUser.getCurrentUser();
                if (!projectNameText.isEmpty()) {
                    AVObject object = new AVObject("ProjectData");
                    object.put("projectName", projectNameText);
                    object.put("projectDescribe", projectDescribeText);
                    object.put("creator", currentUser);
                    ArrayList<Object> member = new ArrayList<>();
                    member.add(currentUser);
                    object.put("member", member);
                    //project state:0-已创建，1-已关闭。
                    object.put("state", 0);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "ProjectDataStoreHelper save project data success.");
                                final CustomAlertDialog customAlertDialog = new CustomAlertDialog(CreateTasksActivity.this);
                                customAlertDialog.setSuccessAlertDialog();
                                customAlertDialog.setText("创建项目成功");
                                Button sure = customAlertDialog.getButton();
                                sure.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        customAlertDialog.getAlertDialog().dismiss();
                                    }
                                });;
                            } else {
                                Log.v(TAG, "ProjectDataStoreHelper save project data fail.error = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "please input project name");
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(v.getContext());
                    customAlertDialog.setSuccessAlertDialog();
                    customAlertDialog.setText("please input project name");
                }
                break;

            case R.id.bt_create_task:
                final String taskNameText = taskName.getText().toString();
                final String taskDescribeText = taskDescribe.getText().toString();
                if (!taskNameText.isEmpty()) {
                    if (mChoiceProjectName.getText() != null) {
                        String selectedProjectText = mChoiceProjectName.getText().toString();
                        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
                        query.whereEqualTo("projectName", selectedProjectText);
                        query.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if (e == null) {
                                    Log.v(TAG, "getAVObject success");
                                    AVObject object = new AVObject("TaskData");
                                    object.put("taskName", taskNameText);
                                    object.put("taskDescribe", taskDescribeText);
                                    AVUser currentUser = AVUser.getCurrentUser();
                                    object.put("creator", currentUser);
                                    object.put("project", avObject);
                                    //task state:0-未领取，1-已领取，2-已关闭。
                                    object.put("state", 0);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                Log.v(TAG, "TaskDataStoreHelper save task data success.");
                                            } else {
                                                Log.v(TAG, "TaskDataStoreHelper save task data fail.error = " + e.getMessage());
                                            }
                                        }
                                    });
                                } else {
                                    new AlertDialog.Builder(CreateTasksActivity.this)
                                            .setIconAttribute(android.R.attr.alertDialogIcon)
                                            .setTitle("创建项目失败。" + "失败原因："+e.getMessage())
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.dismiss();
                        /* User clicked OK so do some stuff */
                                                }
                                            }).show();
                                    Log.v(TAG, "getAVObject fail.error = " + e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.v(TAG, "please input task name");
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(v.getContext());
                    customAlertDialog.setSuccessAlertDialog();
                    customAlertDialog.setText("please input task name");
                }
                break;
            default:
                break;
        }
    }
}
