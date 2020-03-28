package com.rcklos.lentme.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.R;
import com.rcklos.lentme.dataOld.constPool;
import com.rcklos.lentme.utilsOld.LogUtil;
import com.rcklos.lentme.utils.OnPermissionListener;

public class SplashActivity extends PermissionActivity {
    protected static String TAG = constPool.TAG + "Splash";

    public static boolean SIGN_NETWORK = false;

    String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE,//写权限
                            Manifest.permission.READ_EXTERNAL_STORAGE,//读权限
                            Manifest.permission.CAMERA, //相机权限
                            Manifest.permission.READ_PHONE_STATE//读取手机
                            };

    private Handler handler = new Handler();

    /**
     * 检查网络是否连接
     * @return
     */
    public static boolean checkedNetwork(BaseActivity baseActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) baseActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
            baseActivity.show("你已经进入没有网络的异次元世界￣ ￣)σ");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //最首要的调试输出工具初始化
        LogUtil.loadPrinter(new LogUtil.myPrinter() {
            @Override
            public void print(String text) {
                Log.e("LogLentMe",text);
            }
        });

        reqPermission(permissions, new OnPermissionListener() {
            //请求软件所需权限
            @Override
            public void onGranted() {
                Init();
            }

            @Override
            public void onDenied() {
                show("没有给权限会影响app的功能运行哦!");
                Init();
            }
        });
    }

    private void Init(){
        //APP初始化部分

        SIGN_NETWORK = checkedNetwork(this);

        if ( SIGN_NETWORK ){
            LogUtil.i(TAG,"网络连接成功");
            ClientTransponder.getInstance();//实际上是在初始化
        }
        gotoActivity();
    }

    private void gotoActivity(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                animFade();
                finish();
            }
        },2000);
        //可以拓展动画
    }

}
