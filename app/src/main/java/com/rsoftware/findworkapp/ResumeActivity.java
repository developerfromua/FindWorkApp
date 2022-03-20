package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResumeActivity extends AppCompatActivity {
    private String docId;
    private EditText editTextEditResumeName;
    private EditText editTextEditResumeSurname;
    private EditText editTextEditResumeMiddleName;
    private EditText editTextEditResumeWantedVacancy;
    private EditText editTextEditResumeWantedSalary;
    private Spinner spinnerEditResumeBusiness;
    private Spinner spinnerEditResumeSchedule;
    private EditText editTextEditResumePhone;
    private EditText editTextEditResumeEmail;
    private EditText editTextEditResumeCity;
    private EditText editTextEditResumeCitizenship;
    private Spinner spinnerEditResumeSex;
    private Spinner spinnerEditResumeEducation;
    private EditText editTextEditResumeWorkExp;
    private EditText editTextEditResumeEducationInstitution;
    private EditText editTextEditResumeFactuality;
    private EditText editTextEditResumeEducationSpeciality;
    private EditText editTextEditResumeYearEndingEducation;
    private Spinner spinnerEditResumeEducationForm;
    private EditText editTextEditResumeSkills;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextEditResumeName = findViewById(R.id.editTextEditResumeName);
        editTextEditResumeSurname = findViewById(R.id.editTextEditResumeSurname);
        editTextEditResumeMiddleName = findViewById(R.id.editTextEditResumeMiddleName);
        editTextEditResumeWantedVacancy = findViewById(R.id.editTextEditResumeWantedVacancy);
        editTextEditResumeWantedSalary = findViewById(R.id.editTextEditResumeWantedSalary);
        spinnerEditResumeBusiness = findViewById(R.id.spinnerEditResumeBusiness);
        spinnerEditResumeSchedule = findViewById(R.id.spinnerEditResumeSchedule);
        editTextEditResumePhone = findViewById(R.id.editTextEditResumePhone);
        editTextEditResumeEmail = findViewById(R.id.editTextEditResumeEmail);
        editTextEditResumeCity = findViewById(R.id.editTextEditResumeCity);
        editTextEditResumeCitizenship = findViewById(R.id.editTextEditResumeCitizenship);
        spinnerEditResumeSex = findViewById(R.id.spinnerEditResumeSex);
        spinnerEditResumeEducation = findViewById(R.id.spinnerEditResumeEducation);
        editTextEditResumeWorkExp = findViewById(R.id.editTextEditResumeWorkExp);
        editTextEditResumeEducationInstitution = findViewById(R.id.editTextEditResumeEducationInstitution);
        editTextEditResumeFactuality = findViewById(R.id.editTextEditResumeFactuality);
        editTextEditResumeEducationSpeciality = findViewById(R.id.editTextEditResumeEducationSpeciality);
        editTextEditResumeYearEndingEducation = findViewById(R.id.editTextEditResumeYearEndingEducation);
        spinnerEditResumeEducationForm = findViewById(R.id.spinnerEditResumeEducationForm);
        editTextEditResumeSkills = findViewById(R.id.editTextEditResumeSkills);

        docId = getIntent().getStringExtra("docId");

        editTextEditResumeName.setText(getIntent().getStringExtra("name"));
        editTextEditResumeSurname.setText(getIntent().getStringExtra("surname"));
        editTextEditResumeMiddleName.setText(getIntent().getStringExtra("middleName"));
        editTextEditResumeWantedVacancy.setText(getIntent().getStringExtra("wantedVacancy"));
        editTextEditResumeWantedSalary.setText(getIntent().getStringExtra("wantedSalary"));
        spinnerEditResumeBusiness.setSelection(((ArrayAdapter) spinnerEditResumeBusiness.getAdapter()).getPosition(getIntent().getStringExtra("business")));
        spinnerEditResumeSchedule.setSelection(((ArrayAdapter)spinnerEditResumeSchedule.getAdapter()).getPosition(getIntent().getStringExtra("schedule")));
        editTextEditResumePhone.setText(getIntent().getStringExtra("phone"));
        editTextEditResumeEmail.setText(getIntent().getStringExtra("email"));
        editTextEditResumeCity.setText(getIntent().getStringExtra("city"));
        editTextEditResumeCitizenship.setText(getIntent().getStringExtra("citizenship"));
        spinnerEditResumeSex.setSelection(((ArrayAdapter)spinnerEditResumeSex.getAdapter()).getPosition(getIntent().getStringExtra("sex")));
        spinnerEditResumeEducation.setSelection(((ArrayAdapter)spinnerEditResumeEducation.getAdapter()).getPosition(getIntent().getStringExtra("education")));
        editTextEditResumeWorkExp.setText(getIntent().getStringExtra("workExp"));
        editTextEditResumeEducationInstitution.setText(getIntent().getStringExtra("educationInstitution"));
        editTextEditResumeFactuality.setText(getIntent().getStringExtra("factuality"));
        editTextEditResumeEducationSpeciality.setText(getIntent().getStringExtra("educationSpeciality"));
        editTextEditResumeYearEndingEducation.setText(getIntent().getStringExtra("yearEndingEducation"));
        spinnerEditResumeEducationForm.setSelection(((ArrayAdapter)spinnerEditResumeEducationForm.getAdapter()).getPosition(getIntent().getStringExtra("educationForm")));
        editTextEditResumeSkills.setText(getIntent().getStringExtra("skills"));


    }

    public void onClickEditResume(View view) {

        Map<String, Object> data = new HashMap<>();
        data.put("meta_uid", mAuth.getUid());
        data.put("name", editTextEditResumeName.getText().toString().trim());
        data.put("surname", editTextEditResumeSurname.getText().toString().trim());
        data.put("middle_name", editTextEditResumeMiddleName.getText().toString().trim());
        data.put("wanted_vacancy", editTextEditResumeWantedVacancy.getText().toString().trim());
        data.put("wanted_salary", editTextEditResumeWantedSalary.getText().toString().trim());
        data.put("business", spinnerEditResumeBusiness.getSelectedItem().toString().trim());
        data.put("schedule", spinnerEditResumeSchedule.getSelectedItem().toString().trim());
        data.put("phone", editTextEditResumePhone.getText().toString().trim());
        data.put("email", editTextEditResumeEmail.getText().toString().trim());
        data.put("city", editTextEditResumeCity.getText().toString().trim());
        data.put("citizenship", editTextEditResumeCitizenship.getText().toString().trim());
        data.put("sex", spinnerEditResumeSex.getSelectedItem().toString().trim());
        data.put("education", spinnerEditResumeEducation.getSelectedItem().toString().trim());
        data.put("work_exp", editTextEditResumeWorkExp.getText().toString().trim());
        data.put("education_institution", editTextEditResumeEducationInstitution.getText().toString().trim());
        data.put("factuality", editTextEditResumeFactuality.getText().toString().trim());
        data.put("education_speciality", editTextEditResumeEducationSpeciality.getText().toString().trim());
        data.put("year_ending_education", editTextEditResumeYearEndingEducation.getText().toString().trim());
        data.put("education_form", spinnerEditResumeEducationForm.getSelectedItem().toString().trim());
        data.put("skills", editTextEditResumeSkills.getText().toString().trim());

        db.collection("resumes").document(docId)
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResumeActivity.this, "Edited resume", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        finish();
    }

    public void onClickDeleteResume(View view) {db.collection("resumes").document(docId)
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finish();
                    Toast.makeText(ResumeActivity.this, "Deleted resume", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Error deleting document", e);
                }
            });
    }
}