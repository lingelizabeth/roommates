package com.example.roommatehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    public static final String TAG = "CameraActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 40;
    protected File photoFile;
    protected String photoFileName = "photo.jpg";
    private Button btnAdd, btnSkip, btnSave, btnAddAgain;
    private TextView tv1, tv2;
    private ImageView ivIcon, ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnAdd = findViewById(R.id.btnAdd);
        btnSkip = findViewById(R.id.btnSkip);
        btnSave = findViewById(R.id.btnSave);
        btnAddAgain = findViewById(R.id.btnAddAgain);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv1);
        ivIcon = findViewById(R.id.ivIcon);
        ivProfileImage = findViewById(R.id.ivIcon);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileImage(photoFile);
            }
        });
        btnAddAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

    }

    protected void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider.roommatehub", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if request code matches and user took the picture (RESULT_OK)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk, decode it
                Bitmap rawTakenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                Log.i(TAG, "image bytes: "+rawTakenImage.getByteCount());
                // RESIZE BITMAP, see section below
                // Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 200);
                // Load the taken image into a preview
                // Hide all the original elements
                ivIcon.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                btnAdd.setVisibility(View.GONE);
                btnSkip.setVisibility(View.GONE);

                // Display the new profile photo and save/retake buttons
                ivProfileImage.setVisibility(View.VISIBLE);
                ivProfileImage.setImageBitmap(rawTakenImage);
                btnSave.setVisibility(View.VISIBLE);
                btnAddAgain.setVisibility(View.VISIBLE);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void saveProfileImage(File photoFile) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profileImage", new ParseFile(photoFile));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "error saving profile image: "+e);
                    Toast.makeText(CameraActivity.this, e+"", Toast.LENGTH_SHORT);
                    return;
                }
                Log.i(TAG, "Post save successful");
                ivProfileImage.setImageResource(0);
                goMainActivity();
            }
        });
    }

    // Intent to Main Activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}