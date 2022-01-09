package com.caffeine.frient.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caffeine.frient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CircleImageView profile;
    private LinearLayout live, photos, room;
    private TextView post;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        gettingLayoutIDs();
        onClickListeners();
        getUserInfo();

        return view;
    }

    private void gettingLayoutIDs(){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        profile = view.findViewById(R.id.profile_picture);
        live = view.findViewById(R.id.live);
        photos = view.findViewById(R.id.photos);
        room = view.findViewById(R.id.room);
        post = view.findViewById(R.id.post);
    }

    private void onClickListeners(){
        profile.setOnClickListener(v -> {
            //intent to profile
        });

        live.setOnClickListener(v -> {
            //intent to live
        });

        photos.setOnClickListener(v -> {
            //intent to Photos
        });

        room.setOnClickListener(v -> {
            //intent to Room
        });

        post.setOnClickListener(v -> {
            //intent to Post
        });
    }

    private void intent(Class c){
        getActivity().startActivity(new Intent(getActivity(), c));
    }

    private void getUserInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DP = snapshot.child("dp").getValue(String.class);

                if (!DP.equals("empty")){
                    Picasso.get().load(DP).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}