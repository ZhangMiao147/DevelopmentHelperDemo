package com.zhangmiao.developmenthelperdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG="DevelopmentHelperDemo";

    private Button existingTasksButton;
    private Button createTasksButton;
    private Button taskStatisticsButton;
    private Button myTasksButton;
    private Button personalCenterButton;

    private AVUser currentUser;

    private ExistingTasksFragment existingTasksFragment;
    private CreateTasksFragment createTasksFragment;
    private MyTasksFragment myTasksFragment;
    private TaskStatisticsFragment taskStatisticsFragment;
    private PersonalCenterFragment personalCenterFragment;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "EcFJGH8b65CdkW9EMnB5RyjA-gzGzoHsz", "GxioJBEEkj5DHvMWzkMhcaqS");
        initView();
        initEvent();
        fragmentManager = getFragmentManager();
    }
    private void initView()
    {
        existingTasksButton = (Button)findViewById(R.id.existing_tasks_button);
        createTasksButton = (Button)findViewById(R.id.create_tasks_button);
        taskStatisticsButton = (Button)findViewById(R.id.task_statistics_button);
        myTasksButton = (Button)findViewById(R.id.my_tasks_button);
        personalCenterButton = (Button)findViewById(R.id.personal_center_button);
    }
    public void initEvent(){
        existingTasksButton.setOnClickListener(this);
        createTasksButton.setOnClickListener(this);
        taskStatisticsButton.setOnClickListener(this);
        myTasksButton.setOnClickListener(this);
        personalCenterButton.setOnClickListener(this);
    }

    private void restartButton(){
        existingTasksButton.setBackgroundColor(0xffffff);
        createTasksButton.setBackgroundColor(0xffffff);
        taskStatisticsButton.setBackgroundColor(0xffffff);
        myTasksButton.setBackgroundColor(0xffffff);
        personalCenterButton.setBackgroundColor(0xffffff);
    }

    @Override
    public void onClick(View v) {
        restartButton();
        switch (v.getId()){
            case R.id.existing_tasks_button:
                setTabSelection(0);
                break;
            case R.id.create_tasks_button:
                setTabSelection(1);
                break;
            case R.id.task_statistics_button:
                setTabSelection(2);
                break;
            case R.id.my_tasks_button:
                setTabSelection(3);
                break;
            case R.id.personal_center_button:
                setTabSelection(4);
                break;
            default:
                break;
        }
    }

    private void setTabSelection(int index){
        restartButton();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:
                existingTasksButton.setBackgroundColor(0xAA70f3ff);
                if(existingTasksFragment == null){
                    existingTasksFragment = new ExistingTasksFragment();
                    transaction.add(R.id.content,existingTasksFragment);
                }else {
                    transaction.show(existingTasksFragment);
                }
                break;
            case 1:
                createTasksButton.setBackgroundColor(0xAA70f3ff);
                if(createTasksFragment == null){
                    createTasksFragment = new CreateTasksFragment();
                    transaction.add(R.id.content,createTasksFragment);
                }else {
                    transaction.show(createTasksFragment);
                }
                break;
            case 2:
                taskStatisticsButton.setBackgroundColor(0xAA70f3ff);
                if(taskStatisticsFragment == null){
                    taskStatisticsFragment = new TaskStatisticsFragment();
                    transaction.add(R.id.content,taskStatisticsFragment);
                }else {
                    transaction.show(taskStatisticsFragment);
                }
                break;
            case 3:
                myTasksButton.setBackgroundColor(0xAA70f3ff);
                if(myTasksFragment == null){
                    myTasksFragment = new MyTasksFragment();
                    transaction.add(R.id.content,myTasksFragment);
                }else {
                    transaction.show(myTasksFragment);
                }
                break;
            case 4:
                personalCenterButton.setBackgroundColor(0xAA70f3ff);
                if(personalCenterFragment == null){
                    personalCenterFragment = new PersonalCenterFragment();
                    transaction.add(R.id.content,personalCenterFragment);
                }else {
                    transaction.show(personalCenterFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(existingTasksFragment != null){
            transaction.hide(existingTasksFragment);
        }
        if(createTasksFragment != null){
            transaction.hide(createTasksFragment);
        }
        if(taskStatisticsFragment != null){
            transaction.hide(taskStatisticsFragment);
        }
        if(myTasksFragment != null){
            transaction.hide(myTasksFragment);
        }
        if(personalCenterFragment != null){
            transaction.hide(personalCenterFragment);
        }
    }
}
