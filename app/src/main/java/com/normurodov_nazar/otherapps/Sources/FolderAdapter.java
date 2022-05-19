package com.normurodov_nazar.otherapps.Sources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.normurodov_nazar.otherapps.Listeners.ItemCLickListener;
import com.normurodov_nazar.otherapps.R;

import java.io.File;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MFolderHolder> {

    final Context context;
    final ArrayList<File> folders;
    final ItemCLickListener listener;

    public FolderAdapter(Context context, ArrayList<File> folders, ItemCLickListener listener) {
        this.context = context;
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MFolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_layout,parent,false);
        return new MFolderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MFolderHolder holder, int position) {
        holder.setFolderName(folders.get(position),listener,position);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class MFolderHolder extends RecyclerView.ViewHolder{
        final TextView folderName;
        public MFolderHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
        }

        public void setFolderName(File data,ItemCLickListener listener,int position){
            folderName.setText(data.getName());
            itemView.setOnClickListener(c-> listener.onItemClick(position,data));
        }
    }
}
