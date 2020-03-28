package com.rcklos.lentme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rcklos.lentme.R;
import com.rcklos.lentme.data.ChatMessage;

import java.util.ArrayList;
import java.util.Random;

public class ChatMsgAdapter extends RecyclerView.Adapter<ViewHolder> {

    Random random = new Random();

    private Context context;
    private static ArrayList<ChatMessage> messages;

    public ChatMsgAdapter(Context context){
        this.context = context;
        if( messages == null ){
            messages = new ArrayList<>();
        } else {
            clearMessage();
        }
        //可拓展加载本地聊天记录
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_msg,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ( !messages.get(position).getFromToken() ){
            holder.layout_left.setVisibility(View.INVISIBLE);
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.text_right.setText(messages.get(position).getContent());
        } else {
            holder.layout_right.setVisibility(View.INVISIBLE);
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.text_left.setText(messages.get(position).getContent());
        }
    }

    public static void addMessage(ChatMessage message){
        messages.add(message);
    }

    public static void removeMessage(int position){
        messages.remove(position);
    }

    public static void clearMessage(){
        messages.clear();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{
    LinearLayout layout_left,layout_right;
    TextView text_left,text_right;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        layout_left = itemView.findViewById(R.id.item_msg_left);
        layout_right = itemView.findViewById(R.id.item_msg_right);
        text_left = itemView.findViewById(R.id.item_tv_left);
        text_right = itemView.findViewById(R.id.item_tv_right);
    }
}
