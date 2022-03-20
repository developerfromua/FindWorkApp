package com.rsoftware.findworkapp.employee_ui.profile;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rsoftware.findworkapp.AddResumeActivity;
import com.rsoftware.findworkapp.MainActivity;
import com.rsoftware.findworkapp.R;
import com.rsoftware.findworkapp.Resume;
import com.rsoftware.findworkapp.ResumeActivity;
import com.rsoftware.findworkapp.ResumeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    private TextView textViewNameSurname;
    private TextView textViewWelcomeLabel;
    private Button buttonAuth;
    private RecyclerView recyclerViewResumes;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    // Progress bar & loaders
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private ShimmerFrameLayout shimmerContainer;
    // Drawer
    private ImageView imageViewDrawerButton;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView textViewDrawerEmail;
    private TextView textViewDrawerNameSurname;
    private ImageView imageViewDrawerProfileImage;
    private FloatingActionButton buttonAddResume;
    private String nameSurname;
    private String email;
    //Resumes
    private List<Resume> resumeList;
    private ResumeAdapter resumeAdapter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        //  progressBar.setVisibility(View.VISIBLE);
        imageView = (ImageView) view.findViewById(R.id.imageViewProfileImage);
        textViewNameSurname = (TextView) view.findViewById(R.id.textViewNameSurname);
        textViewWelcomeLabel = (TextView) view.findViewById(R.id.textViewWelcomeLabel);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        imageViewDrawerButton = (ImageView) view.findViewById(R.id.imageViewDrawerButton);
        buttonAuth = (Button) view.findViewById(R.id.buttonAuth);
        buttonAddResume = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAddResume);
        shimmerContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        recyclerViewResumes = (RecyclerView) view.findViewById(R.id.recyclerViewResumes);
        shimmerContainer.startShimmer();
        resumeList = new ArrayList<>();
        recyclerViewResumes.setLayoutManager(new LinearLayoutManager(view.getContext()));

        resumeAdapter = new ResumeAdapter(resumeList, new ResumeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Resume item, int pos) {
           //     Toast.makeText(getContext(), item.getWantedVacancy() + " - " + pos, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ResumeActivity.class);
                intent.putExtra("docId", item.getDocId());
                intent.putExtra("name", item.getName());
                intent.putExtra("surname", item.getSurname());
                intent.putExtra("middleName", item.getMiddleName());
                intent.putExtra("wantedVacancy", item.getWantedVacancy());
                intent.putExtra("wantedSalary", item.getWantedSalary());
                intent.putExtra("business", item.getBusiness());
                intent.putExtra("schedule", item.getSchedule());
                intent.putExtra("phone", item.getPhone());
                intent.putExtra("email", item.getEmail());
                intent.putExtra("city", item.getCity());
                intent.putExtra("citizenship", item.getCitizenship());
                intent.putExtra("sex", item.getSex());
                intent.putExtra("education", item.getEducation());
                intent.putExtra("workExp", item.getWorkExp());
                intent.putExtra("educationInstitution", item.getEducationInstitution());
                intent.putExtra("factuality", item.getFactuality());
                intent.putExtra("educationSpeciality", item.getEducationSpeciality());
                intent.putExtra("yearEndingEducation", item.getYearEndingEducation());
                intent.putExtra("educationForm", item.getEducationForm());
                intent.putExtra("skills", item.getResumeSkills());
                if (!item.getDocId().isEmpty()) {
                    startActivity(intent);
                }
            }
        });
        recyclerViewResumes.setAdapter(resumeAdapter);
        resumeAdapter.clearItems();

        buttonAddResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddResumeActivity.class));
                getActivity().overridePendingTransition(0, 0);
            }
        });

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        try {
            navigationView = (NavigationView) view.findViewById(R.id.nav_view_employee);
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
                            getActivity().overridePendingTransition(0, 0);
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
                if (mAuth.getCurrentUser() != null) {
                    try {
                        drawerLayout.openDrawer(GravityCompat.END);
                    } catch (Exception e) {
                        Log.i("TAG", e.toString());
                    }
                } else {
                    Toast.makeText(view.getContext(), "Не авторизован", Toast.LENGTH_SHORT).show();
                }
            }
        });

    //    updateUi();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerContainer.startShimmer();
                releaseRecycler();
                updateUi();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeAdapter.clearItems();
        releaseRecycler();
        updateUi();
    }

    private void updateUi() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            textViewWelcomeLabel.setText("Авторизуйтесь, пожалуйста");
            textViewNameSurname.setText("");

            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .75f, ScaleAnimation.RELATIVE_TO_SELF, .75f);
            scale.setDuration(400);
            //         imageView.setImageResource(R.drawable.ic_user_avatar);
            imageView.setAnimation(scale);
            buttonAuth.setVisibility(View.VISIBLE);
            buttonAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            shimmerContainer.hideShimmer();
            //       progressBar.setVisibility(View.GONE);
        } else {
            buttonAuth.setVisibility(View.INVISIBLE);
            DocumentReference docRef = db.collection("employees").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            textViewWelcomeLabel.setText("Добро пожаловать");
                            email = document.get("email").toString();
                            nameSurname = document.get("name").toString() + " " + document.get("surname").toString();
                            textViewNameSurname.setText(nameSurname);
                            textViewDrawerEmail.setText(email);
                            textViewDrawerNameSurname.setText(nameSurname);
                            if (!document.get("image").toString().isEmpty()) {
                                Picasso.with(getContext())
                                        .load(document.get("image").toString())
                                        .into(imageView);
                                Picasso.with(getContext())
                                        .load(document.get("image").toString())
                                        .into(imageViewDrawerProfileImage);
                            }

                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                    //        progressBar.setVisibility(View.GONE);
                    shimmerContainer.hideShimmer();
                }
            });


        }
    }

    public void releaseRecycler() {
        resumeAdapter.clearItems();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        db.collection("resumes")
                .whereEqualTo("meta_uid", mAuth.getUid())
                .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        resumeList.add(new Resume(document.getId(),document.get("name").toString(),document.get("surname").toString(),document.get("middle_name").toString(),document.get("wanted_vacancy").toString(),document.get("wanted_salary").toString(),document.get("business").toString(),document.get("schedule").toString(),document.get("phone").toString(),document.get("email").toString(),document.get("city").toString(),document.get("citizenship").toString(),document.get("sex").toString(),document.get("education").toString(),document.get("work_exp").toString(),document.get("education_institution").toString(),document.get("factuality").toString(),document.get("education_speciality").toString(),document.get("year_ending_education").toString(),document.get("education_form").toString(),document.get("skills").toString()));
                    }
                    resumeAdapter.notifyDataSetChanged();

                }
            }
        });
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