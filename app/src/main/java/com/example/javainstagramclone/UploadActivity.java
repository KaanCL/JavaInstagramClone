package com.example.javainstagramclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.javainstagramclone.databinding.ActivityMainBinding;
import com.example.javainstagramclone.databinding.ActivityUploadBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadActivity extends AppCompatActivity {
    private ActivityUploadBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
  Uri  imageData;
  Bitmap selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void Upload(View view){}


    public void select_Image(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ask permission

                    }
                }).show();

            } else {
                //ask permission

            }

        }else {
            Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
    }


   public void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
               if(o.getResultCode()==RESULT_OK){
                   Intent intentFromResult =o.getData();
                   if(intentFromResult!=null){
                       imageData=intentFromResult.getData();
                       binding.imageView2.setImageURI(imageData);
                       /*
                              try {
                                if(Build.VERSION.SDK_INT>=28) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(UploadActivity.this.getContentResolver(), imageData);
                                    selectedImage = ImageDecoder.decodeBitmap(source);
                                    binding.imageView2.setImageBitmap(selectedImage);
                                }else{
                                    selectedImage=MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(),imageData);
                                    binding.imageView2.setImageBitmap(selectedImage);
                                }
                              }catch (FileNotFoundException e){
                                  e.printStackTrace();

                              }*/

                   }


               }
            }
        });


   }

   public void permissionLauncher(){

   }


    public void back_function(View view){
        Intent intenttofeed = new Intent(UploadActivity.this,FeedActivity.class);
        startActivity(intenttofeed);
    }


}