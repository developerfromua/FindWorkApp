package com.rsoftware.findworkapp.ui.profile;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rsoftware.findworkapp.MainActivity;
import com.rsoftware.findworkapp.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    private TextView textViewNameSurname;
    private TextView textViewEmail;
    private TextView textViewEmailLabel;
    private TextView textViewWelcomeLabel;
    private TextView textViewResLabel;
    private Button button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private ImageView imageViewDrawerButton;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

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
        textViewEmail = (TextView)  view.findViewById(R.id.textViewEmail);
        textViewWelcomeLabel = (TextView) view.findViewById(R.id.textViewWelcomeLabel);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        textViewEmailLabel = (TextView) view.findViewById(R.id.textViewUserEmail);
        textViewResLabel = (TextView) view.findViewById(R.id.textViewUserRes);
        imageViewDrawerButton = (ImageView) view.findViewById(R.id.imageViewDrawerButton);
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);


        imageViewDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
             drawerLayout.openDrawer(GravityCompat.END); }
                catch (Exception e) {
                    Log.i("TAG", e.toString());
                }
            }
        });


        updateUi();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUi();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }


    private void updateUi() {
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
            textViewWelcomeLabel.setText("Авторизуйтесь, пожалуйста");
            textViewNameSurname.setText("");
            textViewEmail.setText("");
            textViewEmailLabel.setText("");
            textViewResLabel.setText("");
            imageView.setImageResource(R.drawable.ic_user_avatar);
            progressBar.setVisibility(View.GONE);
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
            DocumentReference docRef = db.collection("employees").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            textViewWelcomeLabel.setText("Добро пожаловать");
                            textViewNameSurname.setText(document.get("name").toString() + " " + document.get("surname").toString());
                            textViewEmail.setText(document.get("email").toString());
                            if (!document.get("image").toString().isEmpty()) {
                                Picasso.with(getContext())
                                        .load(document.get("image").toString())
                                        .placeholder(R.drawable.ic_user_avatar)
                                        .error(R.drawable.ic_user_avatar)
                                        .into(imageView);
                            }
                            Log.d("TAG", document.get("image").toString());

                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}