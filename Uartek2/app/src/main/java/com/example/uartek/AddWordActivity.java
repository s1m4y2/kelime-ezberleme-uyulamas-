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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddWordActivity extends AppCompatActivity {

    private Spinner spinnerAge, spinnerGender;
    private ArrayAdapter<CharSequence> adapterGender;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private ActivityAddWordBinding binding ;
    Uri imageData ;
    private  FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String subject ;
    private ListenerRegistration listenerRegistration;
    private String email;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddWordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        registerLauncher(); // registerLauncher metodunu onCreate metodunun içinde çağır
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        init();
    }

    private void init() {
        spinnerGender = findViewById(R.id.main_activity_spinnerGender);
        adapterGender = ArrayAdapter.createFromResource(this, R.array.GenderList, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(0).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subject = "0";
            }
        });
    }
    public void add_image(View view){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch( android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLauncher.launch( android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }
    }

    public void registerLauncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imageData = intentFromResult.getData();
                        binding.imageView.setImageURI(imageData);
                    }

                }
            }
        });

        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            //permission granted
                            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activityResultLauncher.launch(intentToGallery);

                        } else {
                            //permission denied
                            Toast.makeText(AddWordActivity.this,"Permisson needed!",Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }


    public void add_word(View view){
         if((imageData!= null) || (subject.equals("0"))){
             UUID uuid = UUID.randomUUID();
             final String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = firebaseStorage.getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            String  English_text = binding.EnglishText.getText().toString();
                            String Turkish_text = binding.TurkishText.getText().toString();
                            String sentences = binding.sentencesText.getText().toString();
                            String collection_name =email;
                            String number_of_correct_guesses = "0";
                            String date_of_correct_guesses = "00/00/0000";
                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("English_text",English_text);
                            postData.put("image_url",downloadUrl);
                            postData.put("Turkish_text",Turkish_text);
                            postData.put("sentences",sentences);
                            postData.put("number_of_correct_guesses",number_of_correct_guesses);
                            postData.put("date_of_correct_guesses",date_of_correct_guesses);
                            postData.put("word_subject",subject);
                            firebaseFirestore.collection(collection_name).add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    turkish_text_add(Turkish_text);
                                    Intent intent = new Intent(AddWordActivity.this,MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddWordActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddWordActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
         }

         else {
             Toast.makeText(AddWordActivity.this, "Fotoğraf veya konu şeçilmedi", Toast.LENGTH_SHORT).show();
         }
    }

    public void turkish_text_add(String Turkish_text) {
        String collectionName = email + "quiz";
        System.out.println(collectionName);

        firebaseFirestore.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    ArrayList<String> Turkish_text_list = (ArrayList<String>) data.get("Turkish_text_list");
                                    if (Turkish_text_list == null) {
                                        Turkish_text_list = new ArrayList<>();
                                    }
                                    Turkish_text_list.add(Turkish_text);
                                    document.getReference().update("Turkish_text_list", Turkish_text_list)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    System.out.println("DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println("Error updating document: " + e);
                                                }
                                            });
                                }
                            }
                        } else {
                            Toast.makeText(AddWordActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void go_back_click(View view){
        Intent intent = new Intent(AddWordActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }


}

