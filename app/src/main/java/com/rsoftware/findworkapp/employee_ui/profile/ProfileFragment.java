package com.rsoftware.findworkapp.employee_ui.profile;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class ProfileFragment extends Fragment{

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    private TextView textViewNameSurname;
    private TextView textViewEmail;
    private TextView textViewEmailLabel;
    private TextView textViewWelcomeLabel;
    private TextView textViewResLabel;
    private Button button;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    // Progress bar & loaders
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    // Drawer
    private ImageView imageViewDrawerButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView textViewDrawerEmail;
    private TextView textViewDrawerNameSurname;
    private ImageView imageViewDrawerProfileImage;

    private String nameSurname;
    private String email;

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
        textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
        textViewWelcomeLabel = (TextView) view.findViewById(R.id.textViewWelcomeLabel);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        textViewEmailLabel = (TextView) view.findViewById(R.id.textViewUserEmail);
        textViewResLabel = (TextView) view.findViewById(R.id.textViewUserRes);
        imageViewDrawerButton = (ImageView) view.findViewById(R.id.imageViewDrawerButton);


        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        try {
            navigationView = (NavigationView) view.findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_logout:
                            signOut(view);
                            break;
                        case R.id.nav_profile_edit:
                            Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_change_profile:
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.END);
                    return false;
                }
            });

            View header = navigationView.getHeaderView(0);
            textViewDrawerEmail = (TextView) header.findViewById(R.id.textViewDrawerEmail);
            textViewDrawerNameSurname = (TextView) header.findViewById(R.id.textViewDrawerNameSurname);
            imageViewDrawerProfileImage = (ImageView) header.findViewById(R.id.imageViewDrawerProfileImage);

        } catch (Exception e) {
            Log.i("TAG", e.toString());
        }

        imageViewDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser()!=null) {
                    try {
                        drawerLayout.openDrawer(GravityCompat.END);
                    } catch (Exception e) {
                        Log.i("TAG", e.toString());
                    }
                }
                else {
                    Toast.makeText(view.getContext(), "Не авторизован", Toast.LENGTH_SHORT).show();
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
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signOut(view);
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
                            email = document.get("email").toString();
                            nameSurname = document.get("name").toString() + " " + document.get("surname").toString();
                            textViewNameSurname.setText(nameSurname);
                            textViewEmail.setText(email);
                            textViewDrawerEmail.setText(email);
                            textViewDrawerNameSurname.setText(nameSurname);
                            if (!document.get("image").toString().isEmpty()) {
                                Picasso.with(getContext())
                                        .load(document.get("image").toString())
                                        .placeholder(R.drawable.ic_user_avatar)
                                        .error(R.drawable.ic_user_avatar)
                                        .into(imageView);
                                Picasso.with(getContext())
                                        .load(document.get("image").toString())
                                        .placeholder(R.drawable.ic_user_avatar)
                                        .error(R.drawable.ic_user_avatar)
                                        .into(imageViewDrawerProfileImage);
                            }

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

    private void signOut(View view) {
        mAuth.signOut();
        FirebaseUser firebaseUserLogged = mAuth.getCurrentUser();
        if (firebaseUserLogged == null) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(view.getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }



}