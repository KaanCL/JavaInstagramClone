
package com.example.javainstagramclone.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.javainstagramclone.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {


    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;


    Bitmap selectedImage;
    Uri imageData;
    private ActivityUploadBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLaunchers();

        firebaseStorage= FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference=firebaseStorage.getReference();




    }


    public void select_image(View view) {
        openGallery();
    }

    public void Upload(View view){

     if(imageData!=null){

         //Universal unique id
         UUID uuid = UUID.randomUUID();
         String imageName = "images/"+ uuid + ".jpg";

       storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               //Dowload url
             StorageReference newReference = firebaseStorage.getReference(imageName);

             newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                 @Override
                 public void onSuccess(Uri uri) {
                  String dowloadUrl = uri.toString();
                  String comment =binding.editTextComment.getText().toString();

                     FirebaseUser user = auth.getCurrentUser();
                     String email = user.getEmail();

                     HashMap<String , Object> postData = new HashMap<>();
                     postData.put("useremail",email);
                     postData.put("dowloadurl",dowloadUrl);
                     postData.put("comment",comment);
                     postData.put("date", FieldValue.serverTimestamp());

                     firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                         @Override
                         public void onSuccess(DocumentReference documentReference) {

                             Intent intent = new Intent(UploadActivity.this, FeedActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             startActivity(intent);

                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                         }
                     });

                 }

             });

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
           }
       });
     }

    }


    private void requestGalleryPermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void openGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intentToGallery);
    }

    private void registerLaunchers() {
        // İzin launcher'ını kaydet
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            // İzin verildiyse galeriye eriş
                            openGallery();
                        } else {
                            Toast.makeText(UploadActivity.this, "Permission needed!", Toast.LENGTH_LONG).show();
                            requestGalleryPermission();
                        }
                    }
                }
        );

        // Activity result launcher'ını kaydet
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intentFromResult = result.getData();
                            imageData = intentFromResult.getData();
                            binding.imageView.setImageURI(imageData);

                         /*   try {
                                // Uri'yi Bitmap'e dönüştürme
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                                makeSmallerImage(selectedImage,300);

                                binding.imageView.setImageBitmap(selectedImage);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/

                        }
                    }
                }
        );
    }

    public Bitmap makeSmallerImage(Bitmap image , int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}