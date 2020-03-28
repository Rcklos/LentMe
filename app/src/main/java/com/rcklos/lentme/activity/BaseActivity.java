package com.rcklos.lentme.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.jaeger.library.StatusBarUtil;
import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTranslucent(this);
        ClientAppStatus.setCurrentActivity(this);
    }

    public void show(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    public void animFade(){
        overridePendingTransition(R.anim.enteralpha, R.anim.exitalpha);
    }

    protected void showSnackbar(View view, String string){
        Snackbar.make(view,string,1000).show();
    }

}
