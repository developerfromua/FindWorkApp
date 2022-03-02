package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextSurname;
    private RadioGroup radioGroupTypeWorker;
    private RadioButton radioButtonRegisterEmployee;
    private RadioButton radioButtonRegisterEmployer;
    private int typeWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextName = findViewById(R.id.editTextRegisterName);
        editTextSurname = findViewById(R.id.editTextRegisterSurname);
        radioButtonRegisterEmployee = findViewById(R.id.radioButtonRegisterEmployee);
        radioButtonRegisterEmployer = findViewById(R.id.radioButtonRegisterEmployer);
        radioGroupTypeWorker = findViewById(R.id.radioGroupTypeWorker);


    }

    public void onClickLogOut(View view) {
       mAuth.signOut();
    }

    private void regNewUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", editTextName.getText().toString());
                            data.put("surname", editTextSurname.getText().toString());
                            data.put("email", email);
                            if (radioGroupTypeWorker.getCheckedRadioButtonId() == R.id.radioButtonRegisterEmployee) {
                                typeWorker = 0;
                            }
                            else {
                                typeWorker = 1;
                            }
                            data.put("type_worker", typeWorker);
                            db.collection("users").document(mAuth.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(RegisterActivity.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                            }

//                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }



    private void setTypeWorker(int i) {
        DocumentReference update_type_worker = db.collection("users").document("users");
        update_type_worker.update("type_worker", i)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
        if (i == 0) {
            Intent intent = new Intent(RegisterActivity.this, WorkActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(RegisterActivity.this, WorkActivity.class);
            startActivity(intent);
        }
    }

    public void onClickFinishRegister(View view) {
        regNewUser();
    }
}