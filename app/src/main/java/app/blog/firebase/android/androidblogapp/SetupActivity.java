package app.blog.firebase.android.androidblogapp;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        circleImageView = findViewById(R.id.setupImage);
        //after clicking  an image we want user to select an image from gallery
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if user has marshmallow version androids os hehe
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //check if permission is granted to read storage if not grant it
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(SetupActivity.this, "You don't have permission to do that.", Toast.LENGTH_SHORT).show();
                        //if u dont  have permission ask for it
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
//                        Toast.makeText(SetupActivity.this, "You already have permission to do that.", Toast.LENGTH_SHORT).show();
                        //if you have permission do the selecting of image from gallery

                    }
                }
            }
        });
    }
}
