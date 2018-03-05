package app.blog.firebase.android.androidblogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //create toolbar variable
    private android.support.v7.widget.Toolbar main_toolbar;
    //logout
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //instantiate toolbar and make the title - to do items in that create menu resource folder in res folder
        //to make the menu items in a toolbar - onCreateOptionsMenu
        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setTitle("Photo Blog");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate toolbar with options menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //everytime on app start check If not register/login send to a start page(Login,register)
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }
    }


    //once an item from the menu is selected(choose which item and stuff)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionLogout:
                logout();
                return true;

            default:
                return false;
        }

    }

    //logout and send  to login page
    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }

    //send to login intent
    private void sendToLogin() {
        Intent intent =  new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        //won't come back to previus intent after clicking back button
        finish();
    }
}
