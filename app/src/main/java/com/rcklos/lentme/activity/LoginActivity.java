package com.rcklos.lentme.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.R;

public class LoginActivity extends BaseActivity {
    private EditText editPhone;
    private Button btnRegist,btnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        editPhone = findViewById(R.id.login_edit_phone);
        btnLogin = findViewById(R.id.login_btn_login);
        btnRegist = findViewById(R.id.login_btn_register);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhone.clearFocus();
                showSnackbar(btnRegist,"服务器还没搭建好怎么注册？？");
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPhone.clearFocus();
                if (TextUtils.isEmpty(editPhone.getText().toString())){
                    showSnackbar(btnLogin,"账号都没输入登毛线！！");
                } else {
                    ClientAppStatus.setCurrentUserID(editPhone.getText().toString());
                    ClientTransponder.getInstance().sendLoginMsg(editPhone.getText().toString());
                }
            }
        });
    }
}
