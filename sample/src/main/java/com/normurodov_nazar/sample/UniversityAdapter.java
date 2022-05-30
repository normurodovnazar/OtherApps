package com.normurodov_nazar.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniversityHolder> {
    final Context context;
    final List<University> universities;

    public UniversityAdapter(Context context, List<University> universities) {
        this.context = context;
        this.universities = universities;
    }

    @NonNull
    @Override
    public UniversityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UniversityHolder(LayoutInflater.from(context).inflate(R.layout.university_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityHolder holder, int position) {
        holder.setUniversity(universities.get(position));
    }

    @Override
    public int getItemCount() {
        return universities!=null ? universities.size() : 0;
    }

    static class UniversityHolder extends RecyclerView.ViewHolder{
        final TextView country,university,state,openWeb;
        final Context context;
        public UniversityHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            country = itemView.findViewById(R.id.country);
            university = itemView.findViewById(R.id.university);
            state = itemView.findViewById(R.id.stateOrProvince);
            openWeb = itemView.findViewById(R.id.openWeb);
        }

        public void setUniversity(University universityData){
            String sCountry = universityData.getCountry(),sUniversity = universityData.getUniversityName(),sState = universityData.getStateOrProvince();
            if (sCountry!=null) country.setText(context.getString(R.string.country,sCountry)); else country.setVisibility(View.GONE);
            if (sUniversity!=null) university.setText(context.getString(R.string.university,sUniversity)); else university.setVisibility(View.GONE);
            if (sState!=null) state.setText(context.getString(R.string.stateOrProvince,sState)); else state.setVisibility(View.GONE);
            String[] urls = universityData.getWebPages();
            if (urls!=null){
                if (urls.length!=0) {
                    openWeb.setOnClickListener(c-> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[0]))));
                } else openWeb.setVisibility(View.GONE);
            } else openWeb.setVisibility(View.GONE);
        }
    }
}
