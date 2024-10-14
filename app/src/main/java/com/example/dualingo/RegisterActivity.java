package com.example.dualingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dualingo.databinding.ActivityRegisterBinding;
import com.example.dualingo.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        binding.btnGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void signup(){
        String email=binding.etUsername.getText().toString();
        String password=binding.etPassword.getText().toString();
        String confirm=binding.etConfirmPassword.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(),"Sai định dạng email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(getApplicationContext(),"Mật khẩu phải ít nhất 6 kí tự",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirm)){
            Toast.makeText(getApplicationContext(),"2 mật khẩu chưa trùng nhau",Toast.LENGTH_SHORT).show();
            return;
        }
        signupWithFirebase(email,password);
    }
    void signupWithFirebase(String email, String password) {
        setInProgress(true);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = authResult.getUser();
                        Toast.makeText(getApplicationContext(), "Success  "+user.getUid()+"   "+user.getEmail().substring(0, user.getEmail().indexOf('@')), Toast.LENGTH_SHORT).show();
                        User userModel = new User(user.getUid(), user.getEmail(), user.getEmail().substring(0, user.getEmail().indexOf('@')), 0L, 0L);
                        FirebaseFirestore.getInstance().collection("users")
                                .document(user.getUid())
                                .set(userModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        setInProgress(false);
                                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                        finish();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setInProgress(Boolean inProgress){
//        if(inProgress){
//            progressBarSignUp.setVisibility(View.VISIBLE);
//            btnSignUp.setVisibility(View.GONE);
//        }
//        else{
//            progressBarSignUp.setVisibility(View.GONE);
//            btnSignUp.setVisibility(View.VISIBLE);
//        }

    }
}