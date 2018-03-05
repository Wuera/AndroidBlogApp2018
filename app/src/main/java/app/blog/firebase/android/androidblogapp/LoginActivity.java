package app.blog.firebase.android.androidblogapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText loginEmail;
    private EditText loginPasswd;
    private Button loginBtn;
    private Button registerBtn;
    private ProgressBar loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.logoEmail);
        loginPasswd = findViewById(R.id.logoPassword);
        loginBtn = findViewById(R.id.loginButton);
        registerBtn = findViewById(R.id.registerButton);
        loginProgress = findViewById(R.id.progressBarLogin);

        //after clicking register button - send to reg act
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        //after clicking  login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the  strings from edittexts
                String email = loginEmail.getText().toString();
                String password = loginPasswd.getText().toString();

                //if email and password are not empty in edit text
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    //after clicking login make progressbar appear
                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                sendToMain();

                            } else {

                                // If sign in fails, display a message to the user.
                                String e = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, e, Toast.LENGTH_SHORT).show();

                            }

                            //make it invisible after finishando lel
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and send to mainactivity if u r.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }

    }

    private void sendToMain() {
        Intent mainIntent =  new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
