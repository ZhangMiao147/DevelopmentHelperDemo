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
import com.meizu.common.widget.TabLayout;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG="DevelopmentHelperDemo";
    private EditText userName;
    private EditText mailbox;
    private EditText firstPassword;
    private EditText secondPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = (EditText)findViewById(R.id.register_user_name);
        mailbox = (EditText)findViewById(R.id.register_mailbox);
        firstPassword = (EditText)findViewById(R.id.register_first_password);
        secondPassword = (EditText)findViewById(R.id.register_second_password);
        Button register = (Button)findViewById(R.id.register_register);
        if(register != null) {
            register.setOnClickListener(registerClcikListener);
        }

    }

    private View.OnClickListener registerClcikListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String firstPasswordText = firstPassword.getText().toString();
            String secondPasswordText = secondPassword.getText().toString();
            if(!firstPasswordText.equals(secondPasswordText)){
                Log.v(TAG,"firstPasswordText is not equal secondPasswordText");
                return;
            }
            String userNameText = userName.getText().toString();
            String mailboxText = mailbox.getText().toString();

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
