package com.zhangmiao.developmenthelperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import flyme.support.v7.widget.MzItemDecoration;
import flyme.support.v7.widget.RecyclerView;

public class MyTasksFragment extends Fragment {

    private static final String TAG = "DevelopmentHelperDemo";

    private MyTaskAdapter mMyTaskAdapter;
    private final List<String> mMyTaskList = new ArrayList<>();
    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_my_tasks,container,false);
        RecyclerView myTaskList = (RecyclerView)mView.findViewById(R.id.my_tasks_rv);
        myTaskList.setLayoutManager(new WrapContentLinearLayoutManager(mView.getContext()));
        mMyTaskAdapter = new MyTaskAdapter();

        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        AVUser currentUser = AVUser.getCurrentUser();
        query.whereEqualTo("finisher", currentUser);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject object : list) {
                        String taskName = object.getString("taskName");
                        mMyTaskList.add(taskName);
                        mMyTaskAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        myTaskList.setAdapter(mMyTaskAdapter);
        MzItemDecoration itemDecoration = new MzItemDecoration(mView.getContext());
        itemDecoration.setDividerPadding(new MzItemDecoration.DividerPadding() {
            @Override
            public int[] getDividerPadding(int position) {
                return new int[]{16, 16};
            }
        });
        myTaskList.addItemDecoration(itemDecoration);
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMyTaskList.clear();
    }

    class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.TaskViewHolder> {
        @Override
        public int getItemCount() {
            return mMyTaskList.size();
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TaskViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.task_item,null, false));
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.tv.setText(mMyTaskList.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "onClick taskName = " + ((TextView) v).getText());
                    Intent intent = new Intent(mView.getContext(), MyTaskInformationActivity.class);
                    intent.putExtra("taskName", ((TextView) v).getText());
                    startActivity(intent);
                }
            });
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
