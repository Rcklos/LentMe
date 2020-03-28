package com.rcklos.lentme.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.R;
import com.rcklos.lentme.utils.MatchUtil;
import com.rcklos.lentme.utils.OnMatchListener;

public class MatchActivity extends BaseActivity {
    MatchUtil matchUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Init();
    }

    private void match(){
        matchUtil = MatchUtil.getInstance(new OnMatchListener() {
            @Override
            public void onMatch() {
                ClientTransponder.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MatchActivity.this , ChatActivity.class) );
                        animFade();
                        finish();
                    }
                });
            }

            @Override
            public void onFail() {
                ClientTransponder.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        show("对方不在线哦！我们稍后再来吧！");
                        ClientTransponder.getInstance().sendMatchCancel();
                        ClientTransponder.getInstance().postDelay(new Runnable() {
                            @Override
                            public void run() {
                                animFade();
                                finish();
                            }
                        },2000);
                    }
                });
            }
        });
        matchUtil.match();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ClientTransponder.getInstance().sendMatchCancel();
    }

    private void Init(){
        //加载页面
        //ProgressBar progressBar = findViewById(R.id.spin_kit);
        //Sprite fadingCircle = new FadingCircle();
        //progressBar.setIndeterminateDrawable(fadingCircle);
        match();
    }
}
