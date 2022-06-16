package com.normurodov_nazar.contactsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MHolder> {

    final Context context;
    final List<Contact> contactList;
    final ClickListener clickListener;

    public MAdapter(Context context, List<Contact> contactList, ClickListener clickListener) {
        this.context = context;
        this.contactList = contactList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MHolder(LayoutInflater.from(context).inflate(R.layout.contact_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        holder.setData(contactList.get(position),clickListener);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class MHolder extends RecyclerView.ViewHolder{
        final TextView name,number;
        final ImageView call;
        public MHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            call = itemView.findViewById(R.id.call);
        }

        public void setData(Contact contact,ClickListener clickListener){
            name.setText(contact.getName());
            number.setText(contact.getNumber());
            call.setOnClickListener(c->clickListener.onClick(contact));
        }
    }
}
