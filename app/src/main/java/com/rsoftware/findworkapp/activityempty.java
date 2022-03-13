
package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class activityempty extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String typeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityempty);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
     //   Log.i("TAG", mAuth.getUid());
        if (firebaseUser==null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            checkUser();
        }
    }
    private void checkUser() {
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        typeUser = document.get("typeUser").toString();
                        Log.i("TAG", typeUser);
                        if (typeUser.equals("employee")) {
                            startActivity(new Intent(activityempty.this, EmployeeWorkActivity.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(activityempty.this, EmployerWorkActivity.class));
                            finish();
                        }
                    }
                      else {
                        startActivity(new Intent(activityempty.this, MainActivity.class));
               //         Log.d("TAG", "No such document");
                        finish();
                    }
                } else {
          //          Log.d("TAG", "get failed with ", task.getException());
                    finish();
                }
            }
        });
        finish();
    }
}