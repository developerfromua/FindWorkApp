package com.rsoftware.findworkapp.employee_ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rsoftware.findworkapp.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imageViewProfileEditPic;
    private EditText editTextEditProfileName;
    private EditText editTextEditProfileSurname;
    public static final int RC_PHOTO_PICKER = 1;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imageViewProfileEditPic = findViewById(R.id.imageViewProfileEditPic);
        imageViewProfileEditPic.setImageResource(R.drawable.ic_user_avatar);
        editTextEditProfileName = findViewById(R.id.editTextEditProfileName);
        editTextEditProfileSurname = findViewById(R.id.editTextEditProfileSurname);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    editTextEditProfileName.setText(document.get("name").toString());
                    editTextEditProfileSurname.setText(document.get("surname").toString());
                    if (!document.get("image").toString().isEmpty()) {
                        Picasso.with(EditProfileActivity.this)
                                .load(document.get("image").toString())
                                .placeholder(R.drawable.ic_user_avatar)
                                .error(R.drawable.ic_user_avatar)
                                .into(imageViewProfileEditPic);
                    }
                }
            }
        });
    }

    public void onClickEditProfile(View view) {
        String name = editTextEditProfileName.getText().toString().trim();
        String surname = editTextEditProfileSurname.getText().toString().trim();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("surname", surname);
        if (!name.isEmpty() && !surname.isEmpty()) {
            DocumentReference reference = db.collection("users").document(mAuth.getUid());
            reference
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            finish();
                            Toast.makeText(EditProfileActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        this.overridePendingTransition(0, 0);
    }

    public void onClickEditProfileChangePhoto(View view) {
     openPhotoPicker();
    }
    private void openPhotoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra("scale", true);
        photoPickerIntent.putExtra("outputX", 1080);
        photoPickerIntent.putExtra("outputY", 1920);
        photoPickerIntent.putExtra("aspectX", 1);
        photoPickerIntent.putExtra("aspectY", 1);
        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, false);
        startActivityForResult(Intent.createChooser(photoPickerIntent,"Complete Action Using"), RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();

            //set the selected image to ImageView
            imageViewProfileEditPic.setImageURI(pickedImage);
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://findwork-24169.appspot.com/");
            StorageReference storageRef = storage.getReference();
            StorageReference imgRef = storageRef.child("user_photos/" + mAuth.getUid() + ".jpg");
            UploadTask uploadTask = imgRef.putFile(pickedImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EditProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfileActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                }
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imgRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.i("TAG", task.getResult().toString());
                        Map<String, Object> data = new HashMap<>();
                        data.put("image", downloadUri.toString());
                        DocumentReference reference = db.collection("users").document(mAuth.getUid());
                        reference
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();
                                        Toast.makeText(EditProfileActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}