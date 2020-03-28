package com.rcklos.lentme.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.rcklos.lentme.ClientAppStatus;
import com.rcklos.lentme.ClientTransponder;
import com.rcklos.lentme.R;
import com.rcklos.lentme.adapter.ChatMsgAdapter;
import com.rcklos.lentme.data.ChatMessage;
import com.rcklos.lentme.utils.chatUtils;

public class ChatActivity extends BaseActivity {

    private Button btn_send;
    private Button btn_return;
    private TextView text_title;
    private EditText edt_msg;
    private RecyclerView rcv_msg;
    //private ScrollView scrollView;
    private LinearLayout layout_edt;

    private chatUtils cUtils;
    private ChatMsgAdapter chatMsgAdapter;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://空信息
                    rcv_msg.scrollToPosition( chatMsgAdapter.getItemCount() - 1 );
                    break;
                case 1://更新timer
                    String time = (String)msg.obj;
                    if ( TextUtils.isEmpty(time) ) return;
                    text_title.setText(time);
                    break;
                case 2://销毁聊天窗口
                    finish();
                    ClientTransponder.getInstance().destroySingleChat();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Init();
    }

    /**
     * 插入消息于聊天界面
     * @param message 消息JSON
     */
    private void insertMessage( String message ){
        ChatMessage cmsg = JSONObject.parseObject(message,ChatMessage.class);
        insertMessage(cmsg);
    }

    /**
     * 插入消息于聊天界面
     * @param cmsg 消息
     */
    private void insertMessage( ChatMessage cmsg ){
        ChatMsgAdapter.addMessage(cmsg);
        chatMsgAdapter.notifyDataSetChanged();
        //下面为动态显示最后一条数据的实现，由于线程不稳定，故而延迟250ms之后显示
        //rcv_msg.scrollToPosition(chatMsgAdapter.getItemCount()-1);
        //scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        handler.sendEmptyMessageDelayed(0,250);
        //因为发送按钮获取了焦点，为方便下次输入自动让edit获取焦点
        if ( !cmsg.getFromToken() )edt_msg.requestFocus();
    }

    /**
     * 发送消息给对方
     * @param content 消息内容
     */
    private void sendMessageToToken(String content){
        ChatMessage cmsg = new ChatMessage(ClientAppStatus.getCurrentChatSignToken(),ClientAppStatus.getCurrentUserID(),content,true);
        ChatMessage cmsg_self = new ChatMessage(ClientAppStatus.getCurrentChatSignToken(),ClientAppStatus.getCurrentUserID(),content,false);
        insertMessage(cmsg_self);
        String json = JSONObject.toJSONString(cmsg);
        ClientTransponder.getInstance().sendMessageBySignToken(json);
    }

    private void Init(){
        //Msg初始化

        cUtils = chatUtils.getInstance();
        cUtils.loadActivity(this);
        cUtils.setHandler(new chatUtils.MsgHandler() {
            @Override
            public void insertMsgFromToken(String msg) {
                insertMessage(msg);
            }

            @Override
            public void destoryChat() {
                showSnackbar(btn_send,"您的好友已离开房间了哦！");
            }
        });

        //组件绑定
        btn_send = findViewById(R.id.chat_btn_send);
        edt_msg = findViewById(R.id.chat_edt_msg);
        rcv_msg = findViewById(R.id.chat_rcv_msg);
        //scrollView = findViewById(R.id.chat_scrollview);
        text_title = findViewById(R.id.chat_tv_title);
        btn_return = findViewById(R.id.chat_btn_return);
        layout_edt = findViewById(R.id.chat_layout_edt);

        //配置RCV
        chatMsgAdapter = new ChatMsgAdapter(this);
        rcv_msg.setLayoutManager(new LinearLayoutManager(this));
        rcv_msg.setAdapter(chatMsgAdapter);

        //配置发送btn
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edt_msg.getText().toString();
                if ( !TextUtils.isEmpty(msg)){
                    edt_msg.setText("");//清空输入
                    sendMessageToToken(msg);
                }
            }
        });

        layout_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让edit获取焦点，旨在能将信息显示到最新，并且能弹出输入法
                edt_msg.requestFocus();
                showSoftInput(ChatActivity.this,edt_msg);
                //显示最新内容
                handler.sendEmptyMessageDelayed(0,250);
            }
        });

        rcv_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //滑动recycleview时隐藏键盘
                hideSoftInput(ChatActivity.this,edt_msg);
                return false;
            }
        });

        edt_msg.addTextChangedListener(new TextWatcher() {
            //EditText 内容监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString() == null || editable.toString().isEmpty()){
                    btn_send.setBackgroundResource(R.drawable.button_gray_corner_shape);
                } else {
                    btn_send.setBackgroundResource(R.drawable.button_blue_corner_selector);
                }
            }
        });

        //配置返回按钮
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new Thread(){
            @Override
            public void run() {
                while(!TextUtils.isEmpty(ClientAppStatus.getCurrentChatSignToken())){
                    long lastTime = System.currentTimeMillis();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = ClientAppStatus.getSingleLastTimer();
                    handler.sendMessage(message);
                    try {
                        sleep(1000 - (System.currentTimeMillis()-lastTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        ClientTransponder.getInstance().showConfirmDialog("确定要抛弃借你时间的人吗？", new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(2);//销毁窗口
            }
        }, true );

    }

    //弹出键盘
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
    //隐藏键盘
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

//————————————————
//    弹出键盘和隐藏键盘方案借鉴于CSDN博客，详见以下声明
//    版权声明：本文为CSDN博主「发条鱼」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/qq_17846019/article/details/86322780

}
