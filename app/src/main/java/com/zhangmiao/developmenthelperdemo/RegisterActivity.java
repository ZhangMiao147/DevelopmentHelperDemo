package com.zhangmiao.developmenthelperdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG="DevelopmentHelperDemo";
    private EditText mUserName;
    private EditText mMailbox;
    private EditText mFirstPassword;
    private EditText mSecondPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = (EditText)findViewById(R.id.register_user_name);
        mMailbox = (EditText)findViewById(R.id.register_mailbox);
        mFirstPassword = (EditText)findViewById(R.id.register_first_password);
        mSecondPassword = (EditText)findViewById(R.id.register_second_password);
        Button register = (Button)findViewById(R.id.register_register);
        if(register != null) {
            register.setOnClickListener(registerClcikListener);
        }

    }

    private View.OnClickListener registerClcikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String firstPasswordText = mFirstPassword.getText().toString();
            String secondPasswordText = mSecondPassword.getText().toString();
            if(!firstPasswordText.equals(secondPasswordText)){
                Log.v(TAG,"firstPasswordText is not equal secondPasswordText");
                return;
            }
            String userNameText = mUserName.getText().toString();
            String mailboxText = mMailbox.getText().toString();

            AVUser user = new AVUser();
            user.setUsername(userNameText);
            user.setPassword(firstPasswordText);
            user.setEmail(mailboxText);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Log.v(TAG, "register success!");
            }else{
                        Log.v(TAG,"register failed,AVException = " + e.getMessage());
                    }
                }
            });
        }
    };
}
