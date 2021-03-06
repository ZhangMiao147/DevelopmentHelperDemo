package com.zhangmiao.developmenthelperdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.meizu.common.util.GradientDrawableFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DevelopmentHelperDemo";
    private AVUser mCurrentUser;

    private EditText mUserName;
    private EditText mPassword;

    private LinearLayout mSignInLayout;
    private CoordinatorLayout mMainContentLayout;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVOSCloud.initialize(this, "EcFJGH8b65CdkW9EMnB5RyjA-gzGzoHsz", "GxioJBEEkj5DHvMWzkMhcaqS");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCurrentUser = AVUser.getCurrentUser();
        mSignInLayout = (LinearLayout) findViewById(R.id.sign_in_layout);
        mMainContentLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        if (mCurrentUser == null && mMainContentLayout != null) {
            mMainContentLayout.setVisibility(View.GONE);
            initSignInLayout();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            mSignInLayout.setVisibility(View.GONE);
            initMainContentLayout();
        }
        initPersonalCenterLayout();
    }

    public void initMainContentLayout() {
        Fragment taskListFragment = new TaskListFragment();
        Fragment taskStatisticsFragment = new TaskStatisticsFragment();
        Fragment myTaskFragment = new MyTasksFragment();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(taskListFragment, "任务列表");
        pagerAdapter.addFragment(taskStatisticsFragment, "任务统计");
        pagerAdapter.addFragment(myTaskFragment, "我的任务");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null && tabs != null) {
            tabs.setupWithViewPager(viewPager);
        }

        FloatingActionButton createButton = (FloatingActionButton) findViewById(R.id.create_button);
        if(createButton != null) {
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CreateTasksActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void initPersonalCenterLayout() {
        if (mCurrentUser != null) {
            TextView personalCenterUserName = (TextView) findViewById(R.id.personal_center_username);
            if(personalCenterUserName != null) {
                personalCenterUserName.setText(mCurrentUser.getUsername());
            }
        }
    }

    public void initSignInLayout() {
        Button signIn = (Button) findViewById(R.id.sign_in_button);
        Button register = (Button) findViewById(R.id.sign_in_register);
        mUserName = (EditText) findViewById(R.id.sign_in_user_name);
        mPassword = (EditText) findViewById(R.id.sign_in_password);
        mUserName.setBackground(getDrawable(R.drawable.mz_edit_text_background_dialog_blue));
        mUserName.setTextColor(Color.BLACK);
        mPassword.setBackground(getDrawable(R.drawable.mz_edit_text_background_dialog_blue));
        mPassword.setTextColor(Color.BLACK);
        Button forgetPassword = (Button) findViewById(R.id.sign_in_forget_password);
        if(signIn != null) {
            signIn.setOnClickListener(signInListener);
            signIn.setBackground(GradientDrawableFactory.getStateListDrawable(getApplicationContext(), 0xFF198DED));
        }
        if(register != null) {
            register.setOnClickListener(registerListener);
        }
        if(forgetPassword != null) {
            forgetPassword.setOnClickListener(forgetPasswordListener);
        }
    }

    View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userNameText = mUserName.getText().toString();
            Log.v(TAG, "username = " + userNameText);
            final String passwordText = mPassword.getText().toString();
            Log.v(TAG, "password = " + passwordText);
            if (!userNameText.isEmpty() && !passwordText.isEmpty()) {
                AVUser.logInInBackground(userNameText, passwordText, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            Log.v(TAG, "sign in success");
                            final CustomAlertDialog customAlertDialog = new CustomAlertDialog(MainActivity.this);
                            customAlertDialog.setSuccessAlertDialog();
                            customAlertDialog.setText("sign in success");
                            Button sure = customAlertDialog.getButton();
                            sure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customAlertDialog.getAlertDialog().dismiss();
                                }
                            });
                            mSignInLayout.setVisibility(View.GONE);
                            mMainContentLayout.setVisibility(View.VISIBLE);
                            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            initPersonalCenterLayout();
                            initMainContentLayout();
                        } else {
                            Log.v(TAG, "sign in fail,error = " + e.getMessage());
                            CustomAlertDialog customAlertDialog = new CustomAlertDialog(MainActivity.this);
                            customAlertDialog.setFailAlertDialog();
                            customAlertDialog.setText("sign in fail");
                            customAlertDialog.setReason(e.getMessage());
                        }
                    }
                });
            }
        }

    };

    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener forgetPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
