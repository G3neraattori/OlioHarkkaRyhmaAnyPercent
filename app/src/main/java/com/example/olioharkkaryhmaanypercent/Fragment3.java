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

import org.w3c.dom.Text;

import java.security.NoSuchAlgorithmException;

public class Fragment3 extends Fragment {

    View view;
    EditText username;
    EditText salari;
    TextView text;

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
        UserData.USER currentUser = MainActivity.userManager.getCurrentUser();
        if (currentUser!=null) {
            Fragment fragment = new Fragment4();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment3, fragment).remove(this).commit();
            user.loadUserData(currentUser.getUsername());
        }
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
            user.loadUserData(name);
        }else{
            if(user.validatePassword(name, pass)){
                MainActivity.userManager.currentUser = new UserData.USER(getContext());
                MainActivity.userManager.setCurrentUser(name);
                Fragment fragment = new Fragment4();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentWindow, fragment);
                fragmentTransaction.commit();
                user.loadUserData(name);
            }else{
                text.setText("Väärä salasana tai käyttäjänimi.");
            }
  //          user.user.setStatus(false);
        }

    }



}
