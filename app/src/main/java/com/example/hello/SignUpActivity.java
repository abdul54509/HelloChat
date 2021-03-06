package com.example.hello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hello.Models.Users;
import com.example.hello.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Your Account");
        progressDialog.setMessage("Patience have a greater results");

      binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              progressDialog.show();
              auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString() , binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      progressDialog.dismiss();

                      if(task.isSuccessful())
                      {
                          Users user = new Users(binding.etUsername.getText().toString(), binding.etEmail.getText().toString()
                                  , binding.etPassword.getText().toString());
                          String id = task.getResult().getUser().getUid();
                          database.getReference().child("Users").child(id).setValue(user);
                          Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                      }
                      else
                      {
                          Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }

                  }
              });
          }
      });
    }
}