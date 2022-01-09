package com.caffeine.frient.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caffeine.frient.Model.UserDetails;
import com.caffeine.frient.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder>{

    ArrayList<UserDetails> list;
    Activity activity;

    public SuggestionAdapter(ArrayList<UserDetails> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fnd_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getDp()).into(holder.picture);
        holder.name.setText(list.get(position).getName());
        holder.add.setOnClickListener(v -> {
            //add fnd
        });

        holder.layout.setOnClickListener(v -> {
            //intent to profile
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout layout;
        CircleImageView picture;
        TextView name, add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.layout);
            picture = itemView.findViewById(R.id.profile_picture);
            name = itemView.findViewById(R.id.name);
            add = itemView.findViewById(R.id.add_fnd_btn);
        }
    }
}
