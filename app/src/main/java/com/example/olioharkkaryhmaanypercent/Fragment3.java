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

import java.security.NoSuchAlgorithmException;

public class Fragment3 extends Fragment {

    View view;
    EditText username;
    EditText salari;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        UserData user = new UserData(requireContext());
        try {
            user.createUser("a", "a");
            user.validatePassword("password", "AA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        username = (EditText) getView().findViewById(R.id.username);
        salari = (EditText) getView().findViewById(R.id.salari);
        Button mButton = (Button) view.findViewById(R.id.kirjautumisNappi);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login(user);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(UserData user) throws NoSuchAlgorithmException {
        user.createUser(String.valueOf(username.getText()), String.valueOf(salari.getText()));
    }



}
