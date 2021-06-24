package com.kmk.motatawera.admin.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //kmk171619@gmail.com
    //K17M16K19
    //
    //github
    //kmk171619@gmail.com
    //K17M16K19a

    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        initNav();
    }


    private void initNav() {

        BottomNavigationView navigation = findViewById(R.id.navigation);





        AppBarConfiguration configuration = new AppBarConfiguration.Builder(

                R.id.nav_users,
                R.id.nav_subject,
                R.id.nav_notification,
                R.id.nav_profile
        ).build();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigation, navController);

        NavigationUI.setupActionBarWithNavController(this, navController, configuration);

        navigation.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.nav_users:
                    navController.navigate(R.id.nav_users);
                    return true;
                case R.id.nav_subject:
                    navController.navigate(R.id.nav_subject);
                    return true;
                case R.id.nav_notification:
                    navController.navigate(R.id.nav_notification);
                    return true;

                case R.id.nav_profile:
                    navController.navigate(R.id.nav_profile);
                    return true;
            }

            return false;
        });

    }

    @Override
    public void onBackPressed() {
        if (navController.getGraph().getStartDestination() == R.id.nav_youtube) finish();
        else super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateToken();
    }

    private void updateToken() {
        if (new CheckInternetConn(this).isConnection())
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.getCurrentUser().getIdToken(true)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentReference db = firestore.collection("admin")
                                        .document(firebaseAuth.getCurrentUser().getUid());
                                Map<String, Object> map = new HashMap<>();
                                map.put("token", task.getResult().getToken());
                                db.update(map);
                            }
                        });
            }
    }
}