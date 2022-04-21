package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText surNameEditText;
    EditText firstNameEditText;
    EditText birthDateEditText;
    FirebaseFirestore fStore;
    String userID;
    private FirebaseAuth mAuth;
    private FirebaseUser USER;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_profile);




    }

    @Override
    protected void onResume() {
        super.onResume();
        surNameEditText = (EditText) findViewById(R.id.editTextSurName);
        firstNameEditText =findViewById(R.id.editTextGivenName);
        birthDateEditText=findViewById(R.id.editTextBirthDay);
        USER = FirebaseAuth.getInstance().getCurrentUser();
        userID = USER.getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                surNameEditText.setText(value.getString("surName"), TextView.BufferType.EDITABLE);
                firstNameEditText.setText(value.getString("firstName"), TextView.BufferType.EDITABLE);
                birthDateEditText.setText(value.getString("birthDay"), TextView.BufferType.EDITABLE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    public void onLogoutClick(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onCurrencyClick(MenuItem item) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void onProfileClick(MenuItem item) {
        //ez lehet üres

    }

    public void onButtonClick(View vie){
        surNameEditText = findViewById(R.id.editTextSurName);
        firstNameEditText =findViewById(R.id.editTextGivenName);
        birthDateEditText=findViewById(R.id.editTextBirthDay);
        String surname = surNameEditText.getText().toString();
        String firstname =firstNameEditText.getText().toString();
        String birthday = birthDateEditText.getText().toString();

        userID = USER.getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String,Object> user = new HashMap<>();
        user.put("surName",surname);
        user.put("firstName",firstname);
        user.put("email",USER.getEmail());
        user.put("birthDay",birthday);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this,"Save",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onButtonDeletClick(View vie){
        DocumentReference documentReference = fStore.collection("users").document(userID);
        USER.delete();
        documentReference.delete();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();


    }
}