package com.rsoftware.findworkapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Resume item, int pos);
    }

    private List<Resume> resumeList = new ArrayList<>();
    private final OnItemClickListener listener;

    @NonNull
    @Override
    public ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_layout, parent, false);
        return new ResumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeViewHolder holder, int position) {
        holder.bind(resumeList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return resumeList.size();
    }

    public void clearItems() {
        resumeList.clear();
        notifyDataSetChanged();
    }

    class ResumeViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewVacancy;
        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVacancy = (TextView) itemView.findViewById(R.id.textViewVacancyName);
        }
        public void bind(final Resume vacancy, final OnItemClickListener listener, final int pos){
           textViewVacancy.setText(vacancy.getWantedVacancy());
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listener.onItemClick(vacancy, pos);
               }
           });
        }
    }

    public void setResumeList(List<Resume> resumeList) {
        this.resumeList = resumeList;
        notifyDataSetChanged();
    }

    public ResumeAdapter(List<Resume> resumeList, OnItemClickListener listener) {
        this.listener = listener;
        this.resumeList = resumeList;
    }
}
