package com.zhangmiao.developmenthelperdemo;

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
import java.util.List;

public class ProjectDataStoreHelper {
    private static final String TAG="DevelopmentHelperDemo";

/*
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
*/
}
