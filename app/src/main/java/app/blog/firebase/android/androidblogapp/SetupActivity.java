package app.blog.firebase.android.androidblogapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private Uri mainImageURI = null;

    private EditText setupName;
    private Button setupSaveBtn;
    private ProgressBar setupProgressBar;

    private StorageReference mStorageRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        setupProgressBar = findViewById(R.id.setupProgressBar);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        setupName = findViewById(R.id.setupName);
        setupSaveBtn = findViewById(R.id.setupButton);

        //after clicking save btn save that to firedb
        setupSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = setupName.getText().toString();

                //if user input and image uri is  not empty
                if (!TextUtils.isEmpty(userName) && mainImageURI != null) {

                    //upload img to firebase - get userID to set the name  of the img to
                    String userID = firebaseAuth.getCurrentUser().getUid();

                    //add progressbar
                    setupProgressBar.setVisibility(View.VISIBLE);

                    //stores img in firebase storage with name of the user ID
                    StorageReference imagePath = mStorageRef.child("Profile_images").child(userID + ".jpg");
                    imagePath.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            //hide progressbar
                            setupProgressBar.setVisibility(View.INVISIBLE);

                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult().getDownloadUrl();
                                Toast.makeText(SetupActivity.this, "The  image is Uploaded", Toast.LENGTH_SHORT).show();

                            } else {
                                String e = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        circleImageView = findViewById(R.id.setupImage);
        //after clicking  an image we want user to select an image from gallery
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if user has marshmallow version androids os hehe
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //check if permission is granted to read  and write storage if not grant it
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(SetupActivity.this, "You don't have permission to do that.", Toast.LENGTH_SHORT).show();
                        //if u dont  have permission ask for it
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {
//                        Toast.makeText(SetupActivity.this, "You already have permission to do that.", Toast.LENGTH_SHORT).show();

                        //if you have permission do the selecting of image from gallery and crop it using  cropping library
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(SetupActivity.this);

                    }
                }
            }
        });
    }

    //returned results of cropped img and put it (Uri) into mainImageURI variable | set the image to cropped img
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                circleImageView.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
