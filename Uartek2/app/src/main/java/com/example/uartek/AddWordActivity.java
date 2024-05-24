package com.example.uartek;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.uartek.databinding.ActivityAddWordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddWordActivity extends AppCompatActivity {

    private ActivityAddWordBinding binding;
    private Uri imageData;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String subject;
    private String email;
    private boolean isUploading = false;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebase();
        initSpinner();
        initLaunchers();
    }

    private void initFirebase() {
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
    }

    private void initSpinner() {
        Spinner spinnerGender = findViewById(R.id.main_activity_spinnerGender);
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this, R.array.GenderList, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subject = "0";
            }
        });
    }

    private void initLaunchers() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), this::handlePermissionResult);
    }

    private void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            imageData = result.getData().getData();
            binding.imageView.setImageURI(imageData);
        }
    }

    private void handlePermissionResult(Boolean isGranted) {
        if (isGranted) {
            openGallery();
        } else {
            Toast.makeText(AddWordActivity.this, "Permission needed!", Toast.LENGTH_LONG).show();
        }
    }

    public void add_image(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(view);
        } else {
            openGallery();
        }
    }

    private void requestPermission(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission", v -> permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    .show();
        } else {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void openGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intentToGallery);
    }

    public void add_word(View view) {
        if (isUploading) return;

        if (imageData != null && !subject.equals("0")) {
            uploadImageAndSaveWord();
        } else {
            Toast.makeText(AddWordActivity.this, "No photo or subject selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndSaveWord() {
        isUploading = true;
        String imageName = "images/" + UUID.randomUUID() + ".jpg";
        storageReference.child(imageName).putFile(imageData)
                .addOnSuccessListener(taskSnapshot -> getDownloadUrl(imageName))
                .addOnFailureListener(e -> handleFailure(e, "Error uploading image"));
    }

    private void getDownloadUrl(String imageName) {
        firebaseStorage.getReference(imageName).getDownloadUrl()
                .addOnSuccessListener(this::saveWord)
                .addOnFailureListener(e -> handleFailure(e, "Error getting download URL"));
    }

    private void saveWord(Uri uri) {
        Map<String, Object> wordData = createWordData(uri.toString());
        firebaseFirestore.collection(email).add(wordData)
                .addOnSuccessListener(documentReference -> handleSuccess(wordData.get("Turkish_text").toString()))
                .addOnFailureListener(e -> handleFailure(e, "Error saving word"));
    }

    private Map<String, Object> createWordData(String imageUrl) {
        Map<String, Object> wordData = new HashMap<>();
        wordData.put("English_text", binding.EnglishText.getText().toString());
        wordData.put("image_url", imageUrl);
        wordData.put("Turkish_text", binding.TurkishText.getText().toString());
        wordData.put("sentences", binding.sentencesText.getText().toString());
        wordData.put("number_of_correct_guesses", "0");
        wordData.put("date_of_correct_guesses", null);
        wordData.put("word_subject", subject);
        return wordData;
    }

    private void handleSuccess(String turkishText) {
        updateTurkishTextList(turkishText);
        startActivity(new Intent(AddWordActivity.this, MenuActivity.class));
        finish();
        isUploading = false;
    }

    private void handleFailure(Exception e, String message) {
        Toast.makeText(AddWordActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        isUploading = false;
    }

    private void updateTurkishTextList(String turkishText) {
        firebaseFirestore.collection(email + "quiz").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            updateDocumentTurkishTextList(document, turkishText);
                        }
                    } else {
                        showToast("Error getting documents: " + task.getException());
                    }
                });
    }

    private void updateDocumentTurkishTextList(DocumentSnapshot document, String turkishText) {
        Map<String, Object> data = document.getData();
        if (data != null) {
            ArrayList<String> turkishTextList = (ArrayList<String>) data.get("Turkish_text_list");
            if (turkishTextList == null) turkishTextList = new ArrayList<>();
            turkishTextList.add(turkishText);

            document.getReference().update("Turkish_text_list", turkishTextList)
                    .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> System.out.println("Error updating document: " + e));
        }
    }

    private void showToast(String message) {
        Toast.makeText(AddWordActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void go_back_click(View view) {
        startActivity(new Intent(AddWordActivity.this, MenuActivity.class));
        finish();
    }
}
