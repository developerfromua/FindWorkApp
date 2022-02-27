package com.rsoftware.findworkapp.ui.fourth;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rsoftware.findworkapp.R;
import com.rsoftware.findworkapp.WorkActivity;

public class FourthFragment extends Fragment {

    private FourthViewModel mViewModel;
    private RecyclerView recyclerViewResume;
    public static FourthFragment newInstance() {
        return new FourthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Toast.makeText(inflater.getContext(), "Opened 4th tab", Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fourth_fragment,container,false);
        recyclerViewResume = view.findViewById(R.id.recyclerViewResume);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FourthViewModel.class);
        // TODO: Use the ViewModel
    }

}