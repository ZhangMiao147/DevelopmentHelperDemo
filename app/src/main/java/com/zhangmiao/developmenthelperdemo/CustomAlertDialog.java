package com.zhangmiao.developmenthelperdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomAlertDialog {
    private final AlertDialog mAlertDialog;
    private boolean mFlag=true;
    public CustomAlertDialog(Context context){
        mAlertDialog = new AlertDialog.Builder(context).create();
        mAlertDialog.show();
    }


    public void setSuccessAlertDialog(){
        mFlag = true;
        mAlertDialog.getWindow().setContentView(R.layout.success_custom_alert_dialog);
        mAlertDialog.getWindow().findViewById(R.id.sure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });
    }

    public void setFailAlertDialog(){
        mFlag = false;
        mAlertDialog.getWindow().setContentView(R.layout.fail_custom_alert_dialog);
        mAlertDialog.getWindow().findViewById(R.id.sure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                });
    }

    public void setText(String text){
        TextView textView = (TextView)mAlertDialog.getWindow().findViewById(R.id.text);
        textView.setText(text);
    }


    public AlertDialog getAlertDialog(){
        return mAlertDialog;
    }

    public void setButton(String text){
        Button button = (Button)mAlertDialog.getWindow().findViewById(R.id.sure);
        button.setText(text);
    }

    public Button getButton()
    {
        return (Button)mAlertDialog.getWindow().findViewById(R.id.sure);
    }

    public void setReason(String reason){
        if(!mFlag) {
            TextView textView = (TextView) mAlertDialog.getWindow().findViewById(R.id.reason);
            textView.setText(reason);
        }
    }
}
