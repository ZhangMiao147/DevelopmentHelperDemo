package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateTasksFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DevelopmentHelperDemo";

    private EditText projectName;
    private EditText projectDescribe;

    private EditText taskName;
    private EditText taskDescribe;
    private static Spinner taskProjectList;
    private static ArrayAdapter<String> projectListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_tasks, container, false);
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.v(TAG, "getProjectList success");
                    for (AVObject object : list) {
                        String projectName = object.getString("projectName");
                        projectListAdapter.add(projectName);
                        int position = projectListAdapter.getPosition(projectName);
                        taskProjectList.setSelection(position);
                    }
                } else {
                    Log.v(TAG, "getProjectList fail.error = " + e.getMessage());
                }
            }
        });
        projectName = (EditText) view.findViewById(R.id.et_project_name);
        projectDescribe = (EditText) view.findViewById(R.id.et_project_describe);

        taskName = (EditText) view.findViewById(R.id.et_task_name);
        taskDescribe = (EditText) view.findViewById(R.id.et_task_describe);
        taskProjectList = (Spinner) view.findViewById(R.id.project_list);

        Button createProject = (Button) view.findViewById(R.id.bt_create_project);
        Button createTask = (Button) view.findViewById(R.id.bt_create_task);
        createTask.setOnClickListener(this);
        createProject.setOnClickListener(this);
        List<String> projectList = new ArrayList<>();

        projectListAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, projectList);
        projectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskProjectList.setAdapter(projectListAdapter);
        return view;
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
                    object.put("projectName",projectNameText);
                    object.put("projectDescribe",projectDescribeText);
                    object.put("creator", currentUser);
                    ArrayList<Object> member = new ArrayList<>();
                    member.add(currentUser);
                    object.put("member", member);
                    /**
                     * project state:0-已创建，1-已关闭。
                     */
                    object.put("state", 0);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "ProjectDataStoreHelper save task data success.");
                                projectListAdapter.add(projectNameText);
                                int position = projectListAdapter.getPosition(projectNameText);
                                taskProjectList.setSelection(position);
                            } else {
                                Log.v(TAG, "ProjectDataStoreHelper save task data fail.error = " + e.getMessage());
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
                    if (taskProjectList.getSelectedItem() != null) {
                        String selectedProjectText = taskProjectList.getSelectedItem().toString();
                        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
                        query.whereEqualTo("projectName",selectedProjectText);
                        query.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                if(e == null) {
                                    Log.v(TAG, "getAVObject success");
                                    AVObject object = new AVObject("TaskData");
                                    object.put("taskName",taskNameText);
                                    object.put("taskDescribe", taskDescribeText);
                                    AVUser currentUser = AVUser.getCurrentUser();
                                    object.put("creator",currentUser);
                                    object.put("project",avObject);
                                    /**
                                     * task state:0-未领取，1-已领取，2-已关闭。
                                     */
                                    object.put("state",0);
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
                                }else {
                                    Log.v(TAG,"getAVObject fail.error = " + e.getMessage());
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
