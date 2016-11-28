package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
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
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import com.jonas.jgraph.graph.NChart;
import com.jonas.jgraph.models.NExcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskStatisticsFragment extends Fragment {

    private static final String TAG = "DevelopmentHelperDemo";

    private TaskStatisticsAdapter mTaskStatisticsAdapter;

    private List<String> mTaskStaticsList;

    private Map<String, Map<String, String>> mProjectMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_task_statistics, container, false);
        mProjectMap = new HashMap<>();

        mTaskStatisticsAdapter = new TaskStatisticsAdapter();
        mTaskStaticsList = new ArrayList<>();

        RecyclerView mTaskStatisticsRecyclerView = (RecyclerView) view.findViewById(R.id.task_statistics_rv);
        mTaskStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mTaskStatisticsRecyclerView.setAdapter(mTaskStatisticsAdapter);

        initData();
        return view;
    }

    private void initData() {
        AVQuery<AVObject> projectQuery = new AVQuery<>("ProjectData");
        projectQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Log.v(TAG, "task list get project data list success.");
                    for (int i = 0; i < list.size(); i++) {
                        final AVObject project = list.get(i);
                        final String[] projectName = new String[]{""};
                        projectName[0] = list.get(i).getString("projectName");
                        mTaskStaticsList.add(projectName[0]);

                        list.get(i).fetchIfNeededInBackground("member", new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                @SuppressWarnings("unchecked")
                                ArrayList<AVUser> memberList = (ArrayList<AVUser>) avObject.getList("member");
                                final Map<String, String> taskNumberMap = new HashMap<>();
                                for (int n = 0; n < memberList.size(); n++) {
                                    taskNumberMap.put(memberList.get(n).getUsername(), 0 + "");
                                }
                                final AVQuery<AVObject> projectQuery = new AVQuery<>("TaskData");
                                projectQuery.whereEqualTo("project", project);
                                projectQuery.findInBackground(new FindCallback<AVObject>() {
                                    @Override
                                    public void done(List<AVObject> list, AVException e) {
                                        for (int m = 0; m < list.size(); m++) {
                                            String finisherName = list.get(m).getString("finisherName");
                                            if (finisherName != null && !finisherName.isEmpty()) {
                                                String taskSize = taskNumberMap.get(finisherName);

                                                taskSize = (Integer.parseInt(taskSize) + 1) + "";
                                                taskNumberMap.put(finisherName, taskSize);
                                            }
                                        }
                                        mProjectMap.put(projectName[0], taskNumberMap);
                                        mTaskStatisticsAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Log.v(TAG, "task list get project data list error.error = " + e.getMessage());
                }
            }
        });
    }

    class TaskStatisticsAdapter extends RecyclerView.Adapter<TaskStatisticsAdapter.TaskStatisticsViewHolder> {
        @Override
        public int getItemCount() {
            return mTaskStaticsList.size();
        }

        @Override
        public TaskStatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TaskStatisticsViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.task_statistics_item, null, false));
        }

        @Override
        public void onBindViewHolder(TaskStatisticsViewHolder holder, int position) {
            holder.tv.setText(mTaskStaticsList.get(position));
            Map<String, String> memberMap = mProjectMap.get(mTaskStaticsList.get(position));
            if (memberMap != null) {
                Set memberSet = memberMap.keySet();
                Iterator member = memberSet.iterator();
                List<NExcel> nExcelList = new ArrayList<>();
                while (member.hasNext()) {
                    String memberName = (String) member.next();
                    int taskSize = Integer.parseInt(memberMap.get(memberName));
                    nExcelList.add(new NExcel(taskSize, memberName));
                }
                holder.chart.setBarStanded(7);
                holder.chart.setNormalColor(Color.parseColor("#089900"));
                holder.chart.cmdFill(nExcelList);
                holder.chart.setBarWidth(20);
                holder.chart.setInterval(70);
                holder.chart.setHCoordinate(100);
                holder.chart.setAbove(0);
            }
        }

        class TaskStatisticsViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            NChart chart;

            public TaskStatisticsViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.task_statistics_project_name);
                chart = (NChart) view.findViewById(R.id.task_statistics_char);
            }
        }
    }
}
