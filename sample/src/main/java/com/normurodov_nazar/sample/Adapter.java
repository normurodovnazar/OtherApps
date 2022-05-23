package com.normurodov_nazar.sample;

import static com.normurodov_nazar.sample.Keys.titles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.MHolder> {
    final Context context;
    final ClickListener clickListener;

    public Adapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(context).inflate(R.layout.titles_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        holder.setData(position,clickListener);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class MHolder extends RecyclerView.ViewHolder{
        final TextView textView;
        public MHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.t);
        }

        public void setData(int position, ClickListener listener){
            textView.setText((position+1)+". "+ titles.get(position));
            textView.setOnClickListener(view -> listener.onItemClick(position));

        }
    }
}
