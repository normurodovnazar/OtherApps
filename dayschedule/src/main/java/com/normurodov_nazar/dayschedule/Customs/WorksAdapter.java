package com.normurodov_nazar.dayschedule.Customs;

import static com.normurodov_nazar.dayschedule.Public.Hey.changeStatus;
import static com.normurodov_nazar.dayschedule.Public.Hey.getStringTime;
import static com.normurodov_nazar.dayschedule.Public.Hey.print;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.normurodov_nazar.dayschedule.ClickListener;
import com.normurodov_nazar.dayschedule.R;

import java.util.ArrayList;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.ScheduleHolder> {
    final Context context;
    final ArrayList<Work> works;
    final ClickListener clickListener;

    public WorksAdapter(Context context, ArrayList<Work> works, ClickListener clickListener) {
        this.context = context;
        this.works = works;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleHolder(LayoutInflater.from(context).inflate(R.layout.schedule_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        holder.setWork(works.get(position),position,clickListener);
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public void addItem(){

    }

    static class ScheduleHolder extends RecyclerView.ViewHolder{
        final TextView order,title,time;
        final CheckBox active;
        final ImageView edit;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            order = itemView.findViewById(R.id.order);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            active = itemView.findViewById(R.id.active);
        }

        public void setWork(Work work,int position,ClickListener clickListener){
            order.setText("1");
            title.setText(work.getTitle());
            time.setText(itemView.getContext().getString(R.string.time,getStringTime(work)));
            active.setChecked(work.isActive());
            active.setOnClickListener(c-> {
                changeStatus(active.getContext(), work.getId(),active.isChecked());
            });
            edit.setOnClickListener(c->{
                clickListener.onItemClick(position,work);
            });
        }
    }
}
