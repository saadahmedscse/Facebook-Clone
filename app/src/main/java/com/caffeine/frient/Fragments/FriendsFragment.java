package com.caffeine.frient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caffeine.frient.Adapter.SuggestionAdapter;
import com.caffeine.frient.Model.UserDetails;
import com.caffeine.frient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    View view;

    private RecyclerView request, suggestion;
    private DatabaseReference ref;
    private ArrayList<UserDetails> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friends, container, false);

        gettingLayoutIDs();
        getSuggestions();

        return view;
    }

    private void gettingLayoutIDs(){
        request = view.findViewById(R.id.friend_request_recycler_view);
        suggestion = view.findViewById(R.id.friend_suggestion_recycler_view);
        request.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestion.setLayoutManager(new LinearLayoutManager(getContext()));

        ref = FirebaseDatabase.getInstance().getReference().child("Frient").child("Users");
        list = new ArrayList<>();
    }

    private void getSuggestions(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    list.add(ds.getValue(UserDetails.class));
                }

                SuggestionAdapter adapter = new SuggestionAdapter(list, getActivity());
                suggestion.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}