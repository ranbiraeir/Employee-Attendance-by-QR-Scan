package com.ranbiraeir.dailyattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_container)
    TextInputLayout emailContainer;

    @BindView(R.id.password_container)
    TextInputLayout passwordContainer;

    @BindView(R.id.edt_email)
    TextInputEditText edtEmail;

    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;

    private boolean validationError = false;

    private FirebaseAuth auth;

    private ProgressDialog progressDialog;

    private static final String TAG = "LoginActivity";

    private final TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s) && Validator.validateEmail(s.toString())) {
                validationError = false;
                emailContainer.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s) && s.length() >= 6) {
                validationError = false;
                passwordContainer.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @OnClick(R.id.btn_login)
    public void onClickLogin() {
        String email = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            validationError = true;
            emailContainer.setError("Email cannot be empty");

        } else if (!Validator.validateEmail(email)) {
            validationError = true;
            emailContainer.setError("Invalid Email");
        }

        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            validationError = true;
            passwordContainer.setError("Password cannot be empty");

        } else if (password.length() < 6) {
            validationError = true;
            passwordContainer.setError("Password should contain at least 6 characters");
        }

        if (validationError) {
            edtEmail.addTextChangedListener(emailTextWatcher);
            edtPassword.addTextChangedListener(passwordTextWatcher);
        }

        if (!validationError) {
            // Removing Error Messages from all fields
            emailContainer.setError(null);
            passwordContainer.setError(null);

            // Setting Up Progress Dialog
            progressDialog.setMessage("Logging you in...");
            progressDialog.show();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login Success: Email: " + auth.getCurrentUser().getEmail());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    Log.d(TAG, "Sign in Failure: " + task.getException());
                    Exception exception = task.getException();
                    if (exception != null)
                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(LoginActivity.this, "Sign-in Failure. Please Try Again!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}