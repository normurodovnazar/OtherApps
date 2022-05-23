package com.normurodov_nazar.otherapps.Sources;

import static com.normurodov_nazar.otherapps.Sources.Hey.getDuration;
import static com.normurodov_nazar.otherapps.Sources.Hey.print;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.normurodov_nazar.otherapps.Listeners.ItemCLickListener;
import com.normurodov_nazar.otherapps.Models.Music;
import com.normurodov_nazar.otherapps.R;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    final Context context;
    final ArrayList<Music> musicList;
    final ItemCLickListener listener;

    public MusicAdapter(Context context, ArrayList<Music> musicList, ItemCLickListener listener) {
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_layout,parent,false);
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        holder.setData(musicList.get(position),listener,position);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    static class MusicHolder extends RecyclerView.ViewHolder{
        final TextView musicTitle,album,duration,orderMusic;
        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            musicTitle = itemView.findViewById(R.id.musicTitle);
            album = itemView.findViewById(R.id.album);
            duration = itemView.findViewById(R.id.duration);
            orderMusic = itemView.findViewById(R.id.orderMusic);
        }

        public void setData(Music music, ItemCLickListener listener,int position){
            musicTitle.setText(music.getTitle());
            album.setText(music.getAlbum());
            duration.setText(getDuration(music.getDuration()));
            orderMusic.setText(String.valueOf(position+1));
            itemView.setOnClickListener(c->listener.onItemClick(position,music));
        }
    }
}
