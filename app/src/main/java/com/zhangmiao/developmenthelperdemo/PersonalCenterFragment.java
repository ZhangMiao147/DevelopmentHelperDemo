package com.zhangmiao.developmenthelperdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * Created by zhangmiao on 2016/11/18.
 */
public class PersonalCenterFragment extends Fragment {

    private static final String TAG="DevelopmentHelperDemo";

    private View view;
    private View signInView;
    private View personalCenterView;
    private AVUser currentUser;


    private Button signIn;
    private Button register;

    private Button forgetPassword;

    private EditText userName;
    private EditText password;

    private LinearLayout personCenterLayout;
    private LinearLayout signInLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUser = AVUser.getCurrentUser();
        view = inflater.inflate(R.layout.activity_personal_center,container,false);
        personCenterLayout = (LinearLayout)view.findViewById(R.id.personal_center_layout);
        signInLayout = (LinearLayout)view.findViewById(R.id.sign_in_layout);
        if(currentUser == null){
            personCenterLayout.setVisibility(View.GONE);
            initSignInLayout();
        }else{
            signInLayout.setVisibility(View.GONE);
            initPersonalCenterLayout();
        }

        return view;
    }

    public void initPersonalCenterLayout(){
        currentUser = AVUser.getCurrentUser();
        if(currentUser != null){
            TextView personalCenterUserName = (TextView)view.findViewById(R.id.personal_center_username);
            personalCenterUserName.setText(currentUser.getUsername());
        }
    }

    public void initSignInLayout(){
        signIn = (Button)view.findViewById(R.id.sign_in_button);
        register = (Button)view.findViewById(R.id.sign_in_register);
        userName = (EditText)view.findViewById(R.id.sign_in_user_name);
        password = (EditText)view.findViewById(R.id.sign_in_password);
        forgetPassword = (Button)view.findViewById(R.id.sign_in_forget_password);
        signIn.setOnClickListener(signInListener);
        register.setOnClickListener(registerListener);
        forgetPassword.setOnClickListener(forgetPasswordListener);
    }

    View.OnClickListener signInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userNameText = userName.getText().toString();
            Log.v(TAG, "username = " + userNameText);
            final String passwordText = password.getText().toString();
            Log.v(TAG, "password = " + passwordText);
            if(userNameText.isEmpty() || passwordText.isEmpty()){
                return;
            }else {
                AVUser.logInInBackground(userNameText, passwordText, new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            Log.v(TAG, "sign in success");
                            final CustomAlertDialog customAlertDialog = new CustomAlertDialog(view.getContext());
                            customAlertDialog.setSuccessAlertDialog();
                            customAlertDialog.setText("sign in success");
                            Button sure = customAlertDialog.getButton();
                            sure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customAlertDialog.getAlertDialog().dismiss();
                                }
                            });
                            signInLayout.setVisibility(View.GONE);
                            personCenterLayout.setVisibility(View.VISIBLE);
                            initPersonalCenterLayout();

                        } else {
                            Log.v(TAG, "sign in fail,error = " + e.getMessage());
                            CustomAlertDialog customAlertDialog = new CustomAlertDialog(view.getContext());
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
            intent.setClass(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener forgetPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(view.getContext(),ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };
}
