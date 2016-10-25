package com.example.bnguyen336.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class PetListActivity extends AppCompatActivity {

    private ImageView petImageView;
    private Uri imageURI; // stores uri to whatever that has been selected
    // Default: "none.png" (R.drawable.none)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        petImageView = (ImageView) findViewById(R.id.petImageView);

        imageURI = getUriToResource(this, R.drawable.none);

        petImageView.setImageURI(imageURI);
    }


    public void selectPetImage(View view) {

        //List of all permissions to request
        ArrayList<String> permList = new ArrayList<>();

        int cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permList.add(Manifest.permission.CAMERA);
        }

        int readPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            permList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        int writePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            permList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        int requestCode = 100;

        if (permList.size() > 0) {
            //Convert array list into array of Strings
            String[] perms = new String[permList.size()];
            //Request permissions from user

            ActivityCompat.requestPermissions(this, permList.toArray(perms), requestCode);
        }

        //If we have all 3 permissions, open Image Gallery
        if (cameraPermission == PackageManager.PERMISSION_GRANTED
                && readPermission == PackageManager.PERMISSION_GRANTED
                && writePermission == PackageManager.PERMISSION_GRANTED) {
            //Use an intent to launch a gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, requestCode);
        } else {
            Toast.makeText(this, "Pet Protector requires permissions to access camera and external" +
                    " storage.", Toast.LENGTH_LONG).show();
        }
    }

    public static Uri getUriToResource(@NonNull Context context, @AnyRes int resId) throws Resources.NotFoundException {
        Resources res = context.getResources();

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId) +
                '/' + res.getResourceTypeName(resId) +
                '/' + res.getResourceEntryName(resId));
    }
}
