package com.bbsbec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyNotificationAdapter extends RecyclerView.Adapter<MyNotificationAdapter.viewHolder> {

    Context context;
    String[] message;
    String[] date;
    LayoutInflater inflater;

    public MyNotificationAdapter(Context ctx, String[] message, String[] date, LayoutInflater layoutInflater) {
        this.message = message;
        this.date = date;
        this.inflater = layoutInflater;
        this.context = ctx;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_list,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.messageText.setText(message[position]);
        holder.dateText.setText(date[position]);
        holder.notificationImage.setImageResource(R.drawable.main_achievement);

    }

    @Override
    public int getItemCount() {
        return message.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView messageText,dateText;
        ImageView notificationImage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.notification_message);
            dateText = itemView.findViewById(R.id.notification_date);
            notificationImage = itemView.findViewById(R.id.notification_image);
        }
    }

}
