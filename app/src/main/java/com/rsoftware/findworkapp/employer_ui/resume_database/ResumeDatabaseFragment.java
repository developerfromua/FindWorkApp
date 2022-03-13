package com.rsoftware.findworkapp.employer_ui.resume_database;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsoftware.findworkapp.R;

public class ResumeDatabaseFragment extends Fragment {

    private ResumeDatabaseViewModel mViewModel;

    public static ResumeDatabaseFragment newInstance() {
        return new ResumeDatabaseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.resume_database_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResumeDatabaseViewModel.class);
        // TODO: Use the ViewModel
    }

}