package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rsoftware.findworkapp.R;
import com.rsoftware.findworkapp.databinding.ActivityAddResumeBinding;

import java.util.HashMap;
import java.util.Map;

public class AddResumeActivity extends AppCompatActivity {

    private EditText editTextAddResumeName;
    private EditText editTextAddResumeSurname;
    private EditText editTextAddResumeMiddleName;
    private EditText editTextAddResumeWantedVacancy;
    private EditText editTextAddResumeWantedSalary;
    private Spinner spinnerAddResumeBusiness;
    private Spinner spinnerAddResumeSchedule;
    private EditText editTextAddResumePhone;
    private EditText editTextAddResumeEmail;
    private EditText editTextAddResumeCity;
    private EditText editTextAddResumeCitizenship;
    private Spinner spinnerAddResumeSex;
    private Spinner spinnerAddResumeEducation;
    private EditText editTextAddResumeWorkExp;
    private EditText editTextAddResumeEducationInstitution;
    private EditText editTextAddResumeFactuality;
    private EditText editTextAddResumeEducationSpeciality;
    private EditText editTextAddResumeYearEndingEducation;
    private Spinner spinnerAddResumeEducationForm;
    private EditText editTextAddResumeSkills;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resume);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextAddResumeName = findViewById(R.id.editTextAddResumeName);
        editTextAddResumeSurname = findViewById(R.id.editTextAddResumeSurname);
        editTextAddResumeMiddleName = findViewById(R.id.editTextAddResumeMiddleName);
        editTextAddResumeWantedVacancy = findViewById(R.id.editTextAddResumeWantedVacancy);
        editTextAddResumeWantedSalary = findViewById(R.id.editTextAddResumeWantedSalary);
        spinnerAddResumeBusiness = findViewById(R.id.spinnerAddResumeBusiness);
        spinnerAddResumeSchedule = findViewById(R.id.spinnerAddResumeSchedule);
        editTextAddResumeWantedSalary = findViewById(R.id.editTextAddResumeWantedSalary);
        editTextAddResumePhone = findViewById(R.id.editTextAddResumePhone);
        editTextAddResumeEmail = findViewById(R.id.editTextAddResumeEmail);
        editTextAddResumeCity = findViewById(R.id.editTextAddResumeCity);
        editTextAddResumeCitizenship = findViewById(R.id.editTextAddResumeCitizenship);
        spinnerAddResumeSex = findViewById(R.id.spinnerAddResumeSex);
        spinnerAddResumeEducation = findViewById(R.id.spinnerAddResumeEducation);
        editTextAddResumeWorkExp = findViewById(R.id.editTextAddResumeWorkExp);
        editTextAddResumeEducationInstitution = findViewById(R.id.editTextAddResumeEducationInstitution);
        editTextAddResumeFactuality = findViewById(R.id.editTextAddResumeFactuality);
        editTextAddResumeEducationSpeciality = findViewById(R.id.editTextAddResumeEducationSpeciality);
        editTextAddResumeYearEndingEducation = findViewById(R.id.editTextAddResumeYearEndingEducation);
        spinnerAddResumeEducationForm = findViewById(R.id.spinnerAddResumeEducationForm);
        editTextAddResumeSkills = findViewById(R.id.editTextAddResumeSkills);
        setExistedValues();
    }

    private void setExistedValues() {
        DocumentReference docRef = db.collection("employees").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    editTextAddResumeName.setText(document.get("name").toString());
                    editTextAddResumeSurname.setText(document.get("surname").toString());
                    editTextAddResumeEmail.setText(document.get("email").toString());
                }
            }
        });
    }

    public void onClickAddResume(View view) {

        Map<String, Object> data = new HashMap<>();
        data.put("meta_uid", mAuth.getUid());
        data.put("name", editTextAddResumeName.getText().toString().trim());
        data.put("surname", editTextAddResumeSurname.getText().toString().trim());
        data.put("middle_name", editTextAddResumeMiddleName.getText().toString().trim());
        data.put("wanted_vacancy", editTextAddResumeWantedVacancy.getText().toString().trim());
        data.put("wanted_salary", editTextAddResumeWantedSalary.getText().toString().trim());
        data.put("business", spinnerAddResumeBusiness.getSelectedItem().toString().trim());
        data.put("schedule", spinnerAddResumeSchedule.getSelectedItem().toString().trim());
        data.put("phone", editTextAddResumePhone.getText().toString().trim());
        data.put("email", editTextAddResumeEmail.getText().toString().trim());
        data.put("city", editTextAddResumeCity.getText().toString().trim());
        data.put("citizenship", editTextAddResumeCitizenship.getText().toString().trim());
        data.put("sex", spinnerAddResumeSex.getSelectedItem().toString().trim());
        data.put("education", spinnerAddResumeEducation.getSelectedItem().toString().trim());
        data.put("work_exp", editTextAddResumeWorkExp.getText().toString().trim());
        data.put("education_institution", editTextAddResumeEducationInstitution.getText().toString().trim());
        data.put("factuality", editTextAddResumeFactuality.getText().toString().trim());
        data.put("education_speciality", editTextAddResumeEducationSpeciality.getText().toString().trim());
        data.put("year_ending_education", editTextAddResumeYearEndingEducation.getText().toString().trim());
        data.put("education_form", spinnerAddResumeEducationForm.getSelectedItem().toString().trim());
        data.put("skills", editTextAddResumeSkills.getText().toString().trim());


        db.collection("resumes").document()
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddResumeActivity.this, "Added resume", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        finish();
    }
}
