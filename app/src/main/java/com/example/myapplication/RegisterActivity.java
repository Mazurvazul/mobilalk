package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY =42069;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogle;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    EditText emailEditTextLogin;
    EditText passwordEditTextLogin;





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notuser_menu, menu);
        return true;
    }
    public void onLoginClick(MenuItem item) {
        finish();

    }
    public void onCurrencyClick(MenuItem item) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            }catch(ApiException e){
                Log.d(LOG_TAG,"Hiba");

            }
        }
    }

    public void firebaseAuthWithGoogle(String token){
        AuthCredential credential = GoogleAuthProvider.getCredential(token,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Login", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG,"Successful login");
                    //TODO USERINTERFACE INTENT
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Log.d(LOG_TAG,"Hiba" + task.getException().getMessage());
                    Toast.makeText(RegisterActivity.this,"Login Failed! Try Again", Toast.LENGTH_SHORT).show();

                }
            }
        });;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        //google login
        GoogleSignInOptions gos =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogle= GoogleSignIn.getClient(this,gos);


        setContentView(R.layout.activity_login);

    }



    public void onRegisterClick(View view){
        setContentView(R.layout.activity_register);
    }

    public void onGoogleLoginClick(View view){
        Intent signIntent = mGoogle.getSignInIntent();
        startActivityForResult(signIntent,123);

    }

    public void onActuallyLoginClick(View view){
        emailEditTextLogin =findViewById(R.id.editTextNameLogin);
        passwordEditTextLogin =findViewById(R.id.editTextPasswordLogin);
        String emailLogin = emailEditTextLogin.getText().toString();
        String passwordLogin = passwordEditTextLogin.getText().toString();

        if(emailEditTextLogin.length()==0){
            emailEditTextLogin.setError("Required!");
            return;
        }else if(passwordEditTextLogin.length()==0){
            passwordEditTextLogin.setError("Required!");
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailLogin).matches()) {
            emailEditTextLogin.setError("Enter valid email!");
            return;
        }

        mAuth.signInWithEmailAndPassword(emailLogin,passwordLogin).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Login", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG,"Successful login");
                    //TODO USERINTERFACE INTENT
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Log.d(LOG_TAG,"Hiba" + task.getException().getMessage());
                    Toast.makeText(RegisterActivity.this,"Login Failed! Try Again", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void onActuallyRegisterClick(View view){


        emailEditText= findViewById(R.id.emailEditText);
        passwordEditText= findViewById(R.id.passwordEditText);
        passwordAgainEditText= findViewById(R.id.passwordAgainEditText);
        String email  = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordConf = passwordAgainEditText.getText().toString();
        if(!password.equals(passwordConf)){
            passwordEditText.setError("Password not match");
            passwordAgainEditText.setError("Password not match");
            //Toast.makeText(this,"Password not match", Toast.LENGTH_SHORT).show();
            return;

        }else if(emailEditText.length()==0){
            emailEditText.setError("Cannot be empty!");
            return;
        }else if(passwordEditText.length()==0){
            passwordEditText.setError("Required!");
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter valid email!");
            return;
        }else if(passwordEditText.length()<6){
            passwordEditText.setError("Password should be at least 6 characters!");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Register Successful!", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG,"Successful");
                    //intent somewhere
                    Intent intent = new Intent(RegisterActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Log.d(LOG_TAG,"Hiba" + task.getException().getMessage());
                    Toast.makeText(RegisterActivity.this,"Register Failed! Try Again", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Log.i(LOG_TAG,"Név: " + userNameEditText.getText().toString());
    }
}