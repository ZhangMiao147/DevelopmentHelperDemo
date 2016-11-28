package com.zhangmiao.developmenthelperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestEmailVerifyCallback;

public class ForgetPasswordActivity extends AppCompatActivity {

    private static final String TAG="DevelopmentHelperDemo";
    private EditText emailbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailbox = (EditText)findViewById(R.id.forget_password_emailbox);
        Button sure = (Button)findViewById(R.id.forget_password_sure);
        if(sure != null) {
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailboxText = emailbox.getText().toString();

                    if (!emailboxText.isEmpty()) {
                        AVUser.requestEmailVerfiyInBackground(emailboxText, new RequestEmailVerifyCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Log.v(TAG, "重置密码成功");
                                    CustomAlertDialog customAlertDialog = new CustomAlertDialog(getApplicationContext());
                                    customAlertDialog.setSuccessAlertDialog();
                                    customAlertDialog.setText("重置密码成功");
                                } else {
                                    Log.v(TAG, "重置密码失败,error = " + e.getMessage());
                                }
                            }
                        });

                    }
                }
            });
        }
    }
}
