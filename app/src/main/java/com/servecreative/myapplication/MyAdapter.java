package com.servecreative.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.servecreative.myapplication.sendbird.CallService;
import com.servecreative.myapplication.sendbird.utils.PrefUtils;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Context context;
    ArrayList<User> UserArrayList;


    public MyAdapter(Context context, ArrayList<User> UserArrayList) {
        this.context = context;
        this.UserArrayList = UserArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        User user=UserArrayList.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());
        holder.voicecall.setOnClickListener(v -> {
            CallService.dial(context, user.getuId(), false);
            PrefUtils.setCalleeId(context, user.getuId());

        });

        holder.videocall.setOnClickListener(v -> {
            CallService.dial(context, user.getuId(), true);
            PrefUtils.setCalleeId(context, user.getuId());

        });

    }

    @Override
    public int getItemCount() {
        return UserArrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone;
        Button voicecall,videocall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phoneno);
            videocall=itemView.findViewById(R.id.videocall);
            voicecall=itemView.findViewById(R.id.voicecallbutton
            );



        }
    }
}
