package com.caffeine.frient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.caffeine.frient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuFragment extends Fragment {

    private CircleImageView profilePicture;
    private String Name;
    private LinearLayout following, follower, saved, settings;
    private TextView logout, name;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        gettingLayoutIDs();
        getUserInfo();

        return view;
    }

    private void gettingLayoutIDs(){
        profilePicture = view.findViewById(R.id.profile_picture);
        name = view.findViewById(R.id.name);
    }

    private void getUserInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users").child(FirebaseAuth.getInstance().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Name = snapshot.child("name").getValue(String.class);
                name.setText(Name);
                Picasso.get().load(snapshot.child("dp").getValue(String.class)).into(profilePicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}