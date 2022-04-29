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

public class Fragment4 extends Fragment {

    View view;
    EditText username;
    EditText salari;
    TextView text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        UserData user = new UserData(requireContext());
        username = (EditText) getView().findViewById(R.id.username);
        salari = (EditText) getView().findViewById(R.id.salari);
        text = (TextView) getView().findViewById(R.id.textView2);
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
        String name = String.valueOf(username.getText());
        String pass = String.valueOf(salari.getText());
        if(!user.getUser(name)){
            user.createUser(name, pass);
//            user.user.setStatus(true);
        }else{
            if(user.validatePassword(name, pass)){

            }else{
                text.setText("Väärä salasana tai käyttäjänimi.");
            }
  //          user.user.setStatus(false);
        }

    }



}