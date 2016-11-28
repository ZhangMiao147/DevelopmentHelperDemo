package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListFragment extends Fragment {

    private static final String TAG = "DevelopmentHelperDemo";
    private View mView;

    private final Map<String, List<String>> mProjectTaskMap = new HashMap<>();

    private ProjectAdapter mProjectAdapter;
    private TaskAdapter mTaskAdapter;
    private final List<String> mProject = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.v(TAG,"TaskListFragment onCreateView");
        mView = inflater.inflate(R.layout.activity_task_list, container, false);

        mTaskAdapter = new TaskAdapter();
        mProjectAdapter = new ProjectAdapter();

        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.project_task_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        recyclerView.setAdapter(mProjectAdapter);

        initData();

        return mView;
    }

    private void initData()
    {
        AVQuery<AVObject> projectQuery = new AVQuery<>("ProjectData");
        projectQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.v(TAG, "task list get project data list success.");
                    for (int i = 0; i < list.size(); i++) {
                        final String[] projectName = new String[]{""};
                        projectName[0] = list.get(i).getString("projectName");
                        mProject.add(projectName[0]);
                        AVQuery<AVObject> query = new AVQuery<>("TaskData");
                        query.whereEqualTo("project", list.get(i));
                        final List<String> taskList = new ArrayList<>();
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e == null) {
                                    for (AVObject object : list) {
                                        String taskName = object.getString("taskName");
                                        taskList.add(taskName);
                                    }
                                    mProjectTaskMap.put(projectName[0], taskList);
                                    mProjectAdapter.notifyDataSetChanged();
                                    mTaskAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } else {
                    Log.v(TAG, "task list get project data list error.error = " + e.getMessage());
                }
            }
        });
    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        @Override
        public int getItemCount() {
            return mProject.size();
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProjectViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.project_task_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            holder.tv.setText(mProject.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "onClick projectName = " + ((TextView) v).getText());
                    Intent intent = new Intent(mView.getContext(), ProjectInformationActivity.class); //显式Intent
                    intent.putExtra("projectName", ((TextView) v).getText());
                    startActivity(intent);
                }
            });

            holder.rv.setLayoutManager(new LinearLayoutManager(mView.getContext()));
            TaskAdapter taskAdapter = new TaskAdapter(mProject.get(position));
            holder.rv.setAdapter(taskAdapter);
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            RecyclerView rv;

            public ProjectViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.project_task_project_name);
                rv = (RecyclerView) view.findViewById(R.id.task_rv);
            }
        }
    }

    class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        List<String> list;
        String projectName;
        TaskAdapter() {
            list = null;
        }

        TaskAdapter(String projectName) {
            this.projectName = projectName;
            list = mProjectTaskMap.get(projectName);
            if (list == null) {
                Log.v(TAG, "list == null");
            } else {
                Log.v(TAG, "list size = " + list.size());
            }
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            } else {
                return list.size();
            }
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TaskViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.task_item, null, false));
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            if (list != null) {
                holder.tv.setText(list.get(position));
                holder.tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v(TAG, "onClick taskName = " + ((TextView) v).getText());
                        Intent intent = new Intent(mView.getContext(), TaskInformationActivity.class); //显式Intent
                        intent.putExtra("taskName", ((TextView) v).getText());
                        intent.putExtra("projectName", projectName);
                        startActivity(intent);
                    }
                });
            }
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public TaskViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.task_item_tv);
            }
        }
    }

}
