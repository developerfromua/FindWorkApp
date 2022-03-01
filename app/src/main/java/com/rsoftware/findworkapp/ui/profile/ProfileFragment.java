package com.rsoftware.findworkapp.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rsoftware.findworkapp.MainActivity;
import com.rsoftware.findworkapp.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    private Button button;
    private FirebaseAuth mAuth;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageViewProfileImage);
        imageView.setImageResource(R.drawable.ic_user_avatar);
        button = (Button) view.findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            button.setText("Войти в аккаунт");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    FirebaseUser firebaseUserLogged = mAuth.getCurrentUser();
                    if (firebaseUserLogged == null) {
                        Toast.makeText(view.getContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(view.getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}