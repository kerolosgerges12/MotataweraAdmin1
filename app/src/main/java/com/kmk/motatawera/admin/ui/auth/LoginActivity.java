package com.kmk.motatawera.admin.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.kmk.motatawera.admin.R;
import com.kmk.motatawera.admin.databinding.ActivityLoginBinding;
import com.kmk.motatawera.admin.ui.MainActivity;
import com.kmk.motatawera.admin.util.CheckInternetConn;

import static com.kmk.motatawera.admin.util.Hide_Keyboard.hideKeyboard;
import static com.kmk.motatawera.admin.util.ShowAlert.SHOW_ALERT;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    // firebase

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // firebase

        firebaseAuth = FirebaseAuth.getInstance();

        int isLogin = getSharedPreferences("login", MODE_PRIVATE).getInt("isLogin", 0);
        if (isLogin == 1) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // to login account
        binding.loginButton.setOnClickListener(v -> {

            if (new CheckInternetConn(this).isConnection()) {
                checkValidation();
            } else {
                SHOW_ALERT(this,getString(R.string.noInternet));
            }
        });

    }

    private void checkValidation() {

        String email = binding.txtEmailLogin.getText().toString().trim();
        String password = binding.txtPassLogin.getText().toString().trim();

        if (email.isEmpty()) {
            binding.txtEmailLogin.requestFocus();
            SHOW_ALERT(this,getString(R.string.emailReq));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmailLogin.requestFocus();
            SHOW_ALERT(this,getString(R.string.invalidEmail));
            return;
        }

        if (password.isEmpty()) {
            binding.txtPassLogin.requestFocus();
            SHOW_ALERT(this,(getString(R.string.passReq)));
            return;
        }


        login(email, password);
    }

    // method to login
    private void login(String email, String password) {

//        String tokin = FirebaseInstanceId.getInstance().getToken();

        hideKeyboard(this);

        // run progress
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.plzWait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        // start login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        getSharedPreferences("login", MODE_PRIVATE)
                                .edit()
                                .putInt("isLogin", 1)
                                .apply();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        progressDialog.dismiss();
                        handelError(task);

                    }

                });
    }

    private void handelError(Task<AuthResult> task) {


        try {

            Exception exception = task.getException();
            assert exception != null;
            throw exception;

        } catch (FirebaseAuthWeakPasswordException e) {

            SHOW_ALERT(this,getString(R.string.weak_password));

        } catch (FirebaseAuthUserCollisionException e) {

            SHOW_ALERT(this,getString(R.string.user_is_signUp));

        } catch (FirebaseNetworkException e) {

            SHOW_ALERT(this,getString(R.string.noInternet));


        } catch (FirebaseAuthInvalidUserException e) {

            SHOW_ALERT(this,getString(R.string.incorrectPass));


        } catch (Exception e) {

            SHOW_ALERT(this,e.getMessage());

        }
    }

}