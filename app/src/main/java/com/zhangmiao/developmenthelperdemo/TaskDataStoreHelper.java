package com.zhangmiao.developmenthelperdemo;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

/**
 * Created by zhangmiao on 2016/11/21.
 */
public class TaskDataStoreHelper {

    private static final String TAG="DevelopmentHelperDemo";

    public static void createTaskData(String taskName,String taskDescribe,AVUser creator){
        AVObject object = new AVObject("TaskData");
        object.put("taskName",taskName);
        object.put("taskDescribe",taskDescribe);
        object.put("creator",creator);
        /**
         * task state:0-未领取，1-已领取，2-已完成，3-已关闭，4-已取消。
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
    }
    public static void addFinisher(String taskName,final AVUser finisher){

        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        query.whereEqualTo("taskName", taskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.v(TAG, "addFinisher getAVObject success");
                    avObject.put("finisher", finisher);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "addFinisher success");
                            } else {
                                Log.v(TAG, "addFinisher fail");
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "addFinisher getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

    public static void addProject(String taskName,final AVObject project){
        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        query.whereEqualTo("taskName", taskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.v(TAG, "addProject getAVObject success");
                    avObject.put("project", project);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "addProject success");
                            } else {
                                Log.v(TAG, "addProject fail.error = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "addProject getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

    public static void setState(String taskName,final int state){
        AVQuery<AVObject> query = new AVQuery<>("TaskData");
        query.whereEqualTo("taskName",taskName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.v(TAG, "TaskDataStoreHelper setState getAVObject success");
                    avObject.put("state", state);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "TaskDataStoreHelper setState success");
                            } else {
                                Log.v(TAG, "TaskDataStoreHelper setState fail.error = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "TaskDataStoreHelper setState getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

}
