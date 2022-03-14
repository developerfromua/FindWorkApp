package com.rsoftware.findworkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private ProgressBar progressBar;
    DialogFragment dialogFragment;
    FragmentManager manager;
    private String typeUser = "";
    private String loginTypeUser = "";
    String emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("typeUser", -1).apply();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
   
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            checkUserMain();

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.i("TAG", "type: " + typeUser);
                            if (typeUser.equals("employee") || typeUser.equals("employer")) {

                                String email = mAuth.getCurrentUser().getEmail();
                                String fullName = mAuth.getCurrentUser().getDisplayName();
                                String image = mAuth.getCurrentUser().getPhotoUrl().toString();
                                String[] separated = fullName.split(" ");
                                String name = separated[0];
                                String surname = separated[1];
                                String collectionPath;
                                Map<String, Object> data = new HashMap<>();

                                data.put("name", name);
                                data.put("surname", surname);
                                data.put("email", email);
                                data.put("image", image);
                                data.put("typeUser", typeUser);

                                db.collection("users").document(mAuth.getUid())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               if (typeUser.equals("employee")) {
                                                   startActivity(new Intent(MainActivity.this, EmployeeWorkActivity.class));
                                                   MainActivity.this.overridePendingTransition(0, 0);
                                                   Log.d("TAG", "DocumentSnapshot successfully written!");
                                               }
                                               else {
                                                   startActivity(new Intent(MainActivity.this, EmployerWorkActivity.class));
                                                   MainActivity.this.overridePendingTransition(0, 0);
                                                   Log.d("TAG", "DocumentSnapshot successfully written!");
                                               }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error writing document", e);
                                                Toast.makeText(MainActivity.this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");

                            }
                            else {
                                mAuth.signOut();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    public void onClickLogIn(View view) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())  {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                DocumentReference docRef = db.collection("users").document(mAuth.getUid());
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            loginTypeUser = document.get("typeUser").toString();
                                            if (loginTypeUser.equals("employee")) {
                                                Log.i("TAG", "Equal");
                                                Intent intent = new Intent(MainActivity.this, EmployeeWorkActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Log.i("TAG", "not equal");
                                                Intent intent = new Intent(MainActivity.this, EmployerWorkActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Неправильный логин или пароль",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else {
            Toast.makeText(MainActivity.this, "Проверьте правильность ввода", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserMain(){
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        typeUser = document.get("typeUser").toString();
                        Log.i("TAG", "from document: " + typeUser);
                        if (typeUser.equals("employee")) {
                            typeUser = "employee";
                            signIn();
                            Log.i("TAG", "set: " + typeUser);
                        }
                        else if (typeUser.equals("employer")){
                            typeUser = "employer";
                            signIn();
                            Log.i("TAG", typeUser);
                        }
                        else {
                            typeUser = "noFieldOrDocument";
                            Log.i("TAG", typeUser);
                            showDialog();
                        }
                    }
                    else {

                        typeUser = "noFieldOrDocument";
                        showDialog();
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    public void onClickLogInGoogle(View view) {
        signIn();

    }

    private void showDialog() {
        dialogFragment = new DialogFragment();
        manager = getSupportFragmentManager();
        dialogFragment.show(manager, "dialog");
    }

    public void employeeClicked() {
        typeUser = "employee";
        signIn();
    }

    public void employerClicked() {
        typeUser = "employer";
        signIn();
    }

}