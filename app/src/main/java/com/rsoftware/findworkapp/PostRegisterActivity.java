package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostRegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    public void onClickLogOut(View view) {
        signOut();
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostRegisterActivity.this, "logged out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PostRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void onClickEmployer(View view) {
        setTypeWorker(1);
    }

    public void onClickEmployee(View view) {
        setTypeWorker(0);
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
            Intent intent = new Intent(PostRegisterActivity.this, WorkActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(PostRegisterActivity.this, WorkActivity.class);
            startActivity(intent);
        }
    }
}