package com.example.olioharkkaryhmaanypercent;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UserpageFragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment4, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.movieRecyclerView);
        RecyclerViewAdapter2 adapter = new RecyclerViewAdapter2(UserData.actuallyLoadUserData(MainActivity.userManager.getCurrentUser().getUsername()).values().toArray(new Movie[0]), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        View.OnClickListener logoutlistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == view.findViewById(R.id.logoutButton)) {
                    MainActivity.userManager.setNullUser();
                    Fragment fragment = new LoginFragment();
                    FragmentManager manager = getParentFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentWindow, fragment);
                    transaction.commit();
                }
            }
        };
        Button logoutbutton = view.findViewById(R.id.logoutButton);
        logoutbutton.setOnClickListener(logoutlistener);
    }

    public void onMovieClick(@NonNull View view, int position) {
        Fragment_user_infopage fragment = new Fragment_user_infopage();
        fragment.setPosition(position);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentWindow, fragment);
        transaction.commit();

    }


}
