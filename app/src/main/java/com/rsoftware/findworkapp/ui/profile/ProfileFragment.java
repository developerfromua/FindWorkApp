package com.rsoftware.findworkapp.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rsoftware.findworkapp.MainActivity;
import com.rsoftware.findworkapp.R;

import jp.wasabeef.blurry.Blurry;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    private TextView textViewNameSurname;
    private TextView textViewEmail;
    private TextView textViewWelcomeLabel;
    private Button button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        imageView = (ImageView) view.findViewById(R.id.imageViewProfileImage);
        imageView.setImageResource(R.drawable.ic_user_avatar);
        button = (Button) view.findViewById(R.id.buttonLogout);
        textViewNameSurname = (TextView) view.findViewById(R.id.textViewNameSurname);
        textViewEmail = (TextView)  view.findViewById(R.id.textViewUserEmail);
        textViewWelcomeLabel = (TextView) view.findViewById(R.id.textViewWelcomeLabel);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            button.setText("Войти или зарегистрироваться");
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
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(view.getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (mAuth.getCurrentUser() != null) {
            textViewWelcomeLabel.setText("Подключение...");
            DocumentReference docRef = db.collection("employees").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            textViewWelcomeLabel.setText("Добро пожаловать, ");
                            textViewNameSurname.setText(document.get("name").toString() + " " + document.get("surname").toString());
                            textViewEmail.append(document.get("email").toString());
                            progressBar.setVisibility(View.GONE);


                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
        }
        else {
            textViewWelcomeLabel.setText("Авторизуйтесь, пожалуйста");
            progressBar.setVisibility(View.GONE);

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