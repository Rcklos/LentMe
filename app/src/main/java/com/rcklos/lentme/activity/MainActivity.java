package com.rcklos.lentme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rcklos.lentme.ClientConfig;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.R;

public class MainActivity extends BaseActivity {

    //组件
    Button btn_match;

    //工具

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    private void Init(){
        //main初始化
        Init_Page_Match();
    }

    private void Init_Page_Match(){
        //绑定组件
        btn_match = findViewById(R.id.main_btn_match);

        //绑定监听器
        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMatch();
            }
        });
    }

    private void onMatch(){//匹配函数

        if (!SplashActivity.SIGN_NETWORK ){
            if (SplashActivity.checkedNetwork(this)) {
                show("网络连接成功！即将重启app( ￣▽￣)σ");
                gotoSplashActivity();
            } else {
                show("网络没连上啊( ‘-ωก̀ )");
            }
            return;
        }
        gotoChatActivity();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void gotoSplashActivity(){
        startActivity(new Intent(this,SplashActivity.class));
        animFade();
        finish();
    }

    private void gotoChatActivity(){
        startActivity(new Intent(this,MatchActivity.class));
        animFade();
    }

}
