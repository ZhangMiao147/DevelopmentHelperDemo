package com.zhangmiao.developmenthelperdemo;

import android.provider.ContactsContract;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangmiao on 2016/11/21.
 */
public class ProjectDataStoreHelper {
    private static final String TAG="DevelopmentHelperDemo";

    public static void createProjectData(String projectName,String projectDescribe, AVUser creator){
        AVObject object = new AVObject("ProjectData");
        object.put("projectName",projectName);
        object.put("projectDescribe",projectDescribe);
        object.put("creator",creator);
        ArrayList<Object> member = new ArrayList<>();
        member.add(creator);
        object.put("member", member);
        /**
         * task state:0-已创建，1-已完成，2-已关闭，3-已取消。
         */
        object.put("state", 0);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.v(TAG, "ProjectDataStoreHelper save task data success.");
                } else {
                    Log.v(TAG, "ProjectDataStoreHelper save task data fail.error = " + e.getMessage());
                }
            }
        });
        CreateTasksFragment.addProjectList(projectName);
    }

    public static void addMember(String projectName,final AVUser member){
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName", projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.v(TAG, "addMember getAVObject success");
                    avObject.addAllUnique("member", Arrays.asList(member));
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "addMember success");
                            } else {
                                Log.v(TAG, "addMember fail.error = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "addMember getAVObject fail.error = " + e.getMessage());
                }
            }
        });

    }

    public static void setState(String projectName,final int state){
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName",projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.v(TAG, "setState getAVObject success");
                    avObject.put("state", state);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Log.v(TAG, "setState success");
                            } else {
                                Log.v(TAG, "setState fail.error = " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.v(TAG, "setState getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

    public static void addSpinnerList(){
        final ArrayList<String> projectList = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null) {
                    Log.v(TAG,"getProjectList success");
                    for (AVObject object : list) {
                        String projectName = object.getString("projectName");
                        projectList.add(projectName);
                        CreateTasksFragment.addProjectList(projectName);
                    }
                }else {
                    Log.v(TAG,"getProjectList fail.error = " + e.getMessage());
                }
            }
        });
    }

    public static void addTaskProject(final String taskName,String projectName)
    {
        AVQuery<AVObject> query = new AVQuery<>("ProjectData");
        query.whereEqualTo("projectName",projectName);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null) {
                    Log.v(TAG,"getAVObject success");
                    TaskDataStoreHelper.addProject(taskName, avObject);
                }else {
                    Log.v(TAG,"getAVObject fail.error = " + e.getMessage());
                }
            }
        });
    }

}
