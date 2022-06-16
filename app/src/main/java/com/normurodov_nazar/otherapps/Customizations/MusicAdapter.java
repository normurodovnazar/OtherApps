package com.normurodov_nazar.otherapps.Customizations;

import static com.normurodov_nazar.otherapps.Customizations.Hey.getDuration;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentPosition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.normurodov_nazar.otherapps.Listeners.ItemCLickListener;
import com.normurodov_nazar.otherapps.Models.Music;
import com.normurodov_nazar.otherapps.Models.Paths;
import com.normurodov_nazar.otherapps.R;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    final Context context;
    final ArrayList<Music> musicList;
    final ItemCLickListener listener;
    final ItemCLickListener removeListener;
    final boolean forPlaylist;


    public MusicAdapter(Context context, ArrayList<Music> musicList, ItemCLickListener listener,ItemCLickListener removeListener,boolean forPlaylist) {
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
        this.forPlaylist = forPlaylist;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_layout,parent,false);
        return new MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
        holder.setData(musicList.get(position),listener,position,forPlaylist,removeListener);
    }

    public void updateItem(){
        musicList.set(currentPosition,currentMusic);
        notifyItemChanged(currentPosition);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    static class MusicHolder extends RecyclerView.ViewHolder{
        final TextView musicTitle,album,duration,orderMusic;
        final ImageView remove;
        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            musicTitle = itemView.findViewById(R.id.musicTitle);
            album = itemView.findViewById(R.id.album);
            duration = itemView.findViewById(R.id.duration);
            orderMusic = itemView.findViewById(R.id.orderMusic);
            remove = itemView.findViewById(R.id.remove);
        }

        public void setData(Music music, ItemCLickListener listener,int position,boolean forPlaylist,ItemCLickListener removeListener){
            musicTitle.setText(music.getTitle());
            album.setText(music.getAlbum());
            duration.setText(getDuration(music.getDuration()));
            orderMusic.setText(String.valueOf(position+1));
            itemView.setOnClickListener(c->listener.onItemClick(position,music));
            if (forPlaylist) {
                remove.setVisibility(View.VISIBLE);
                remove.setOnClickListener(c-> removeListener.onItemClick(position,new Paths(currentMusic.id,currentMusic.getPath())));
            }
        }
    }
}
