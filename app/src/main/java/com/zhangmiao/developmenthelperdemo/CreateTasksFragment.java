package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiao on 2016/11/18.
 */
public class CreateTasksFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DevelopmentHelperDemo";

    private View view;

    private EditText projectName;
    private EditText projectDescribe;

    private EditText taskName;
    private EditText taskDescribe;
    private static Spinner taskProjectList;
    private List<String> projectList;
    private static ArrayAdapter<String> projectListAdapter;

    private Button createProject;
    private Button createTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_create_tasks, container, false);

        projectName = (EditText) view.findViewById(R.id.et_project_name);
        projectDescribe = (EditText) view.findViewById(R.id.et_project_describe);

        taskName = (EditText) view.findViewById(R.id.et_task_name);
        taskDescribe = (EditText) view.findViewById(R.id.et_task_describe);
        taskProjectList = (Spinner) view.findViewById(R.id.project_list);

        createProject = (Button) view.findViewById(R.id.bt_create_project);
        createTask = (Button) view.findViewById(R.id.bt_create_task);
        createTask.setOnClickListener(this);
        createProject.setOnClickListener(this);
        projectList = new ArrayList<>();

        projectListAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, projectList);
        projectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskProjectList.setAdapter(projectListAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ProjectDataStoreHelper.addSpinnerList();
    }

    public static void addProjectList(String projectName) {
        for (int i = 0; i < projectListAdapter.getCount(); i++) {
            if (projectName.equals(projectListAdapter.getItem(i)))
                return;
        }
        projectListAdapter.add(projectName);
        int position = projectListAdapter.getPosition(projectName);
        taskProjectList.setSelection(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_create_project:
                String projectNameText = projectName.getText().toString();
                String projectDescribeText = projectDescribe.getText().toString();
                AVUser currentUser = AVUser.getCurrentUser();
                if (!projectNameText.isEmpty()) {
                    ProjectDataStoreHelper.createProjectData(projectNameText, projectDescribeText, currentUser);
                } else {
                    Log.v(TAG, "please input project name");
                }
                break;

            case R.id.bt_create_task:
                String taskNameText = taskName.getText().toString();
                String taskDescribeText = taskDescribe.getText().toString();

                AVUser avUser = AVUser.getCurrentUser();
                if (!taskNameText.isEmpty()) {
                    TaskDataStoreHelper.createTaskData(taskNameText, taskDescribeText, avUser);
                    if (taskProjectList.getSelectedItem() != null) {
                        String selectedProjectText = taskProjectList.getSelectedItem().toString();
                        ProjectDataStoreHelper.addTaskProject(taskNameText, selectedProjectText);
                    }
                } else {
                    Log.v(TAG, "please input task name");
                }

                break;
            default:
                break;
        }
    }


}
