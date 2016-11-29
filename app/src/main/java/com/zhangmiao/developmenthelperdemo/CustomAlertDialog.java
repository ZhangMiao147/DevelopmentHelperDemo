package com.zhangmiao.developmenthelperdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomAlertDialog {
    private final AlertDialog alertDialog;
    private final AlertDialog.Builder builder;
    private boolean flag=true;
    private Context context;
    public CustomAlertDialog(Context context){
        this.context = context;
        builder = new AlertDialog.Builder(context);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
    }


    public void setSuccessAlertDialog(){
        flag = true;
        alertDialog.getWindow().setContentView(R.layout.success_custom_alert_dialog);
        alertDialog.getWindow().findViewById(R.id.sure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
    }

    public void setFailAlertDialog(){
        flag = false;
        alertDialog.getWindow().setContentView(R.layout.fail_custom_alert_dialog);
        alertDialog.getWindow().findViewById(R.id.sure)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
    }

    public void setText(String text){
        TextView textView = (TextView)alertDialog.getWindow().findViewById(R.id.text);
        textView.setText(text);
    }

    public TextView getTextView(){
        return (TextView)alertDialog.getWindow().findViewById(R.id.text);
    }

    public AlertDialog getAlertDialog(){
        return alertDialog;
    }

    public void setButton(String text){
        Button button = (Button)alertDialog.getWindow().findViewById(R.id.sure);
        button.setText(text);
    }

    public Button getButton()
    {
        return (Button)alertDialog.getWindow().findViewById(R.id.sure);
    }

    public void setReason(String reason){
        if(flag == false) {
            TextView textView = (TextView) alertDialog.getWindow().findViewById(R.id.reason);
            textView.setText(reason);
        }
    }
    public TextView getReason(){
        if(flag == false){
            return (TextView) alertDialog.getWindow().findViewById(R.id.reason);
        }
        return null;
    }

}
