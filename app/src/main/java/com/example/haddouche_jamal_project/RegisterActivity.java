package com.example.haddouche_jamal_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout name,prenom,email,phone,password,filiere;
    Button registreBtn,loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        //initialisation de fiarebase authentification

        name=findViewById(R.id.name);
        prenom=findViewById(R.id.prenom);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        filiere=findViewById(R.id.filiere);
        password=findViewById(R.id.password);
        registreBtn=findViewById(R.id.registre);
        loginBtn=findViewById(R.id.login);



        registreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtName=name.getEditText().getText().toString();
                String txtPrenom=prenom.getEditText().getText().toString();
                String txtEmail=email.getEditText().getText().toString();
                String txtPhone=phone.getEditText().getText().toString();
                String txtPassword=password.getEditText().getText().toString();
                String txtFiliere=filiere.getEditText().getText().toString();

                //tester si les champs sont vides
                if (txtName.isEmpty()){
                    name.setError("nom incorrect!");
                    name.requestFocus();
                    return;
                }
                if (txtPrenom.isEmpty()){
                    prenom.setError("prenom incorrect!");
                    prenom.requestFocus();
                    return;
                }

                if (txtPhone.isEmpty()){
                    phone.setError("phone incorrect");
                    phone.requestFocus();
                    return;
                }
                if (txtFiliere.isEmpty()){
                    filiere.setError("filiere incorrect");
                    filiere.requestFocus();
                    return;
                }

                if (txtEmail.isEmpty()){
                    email.setError("email incorrect!");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){
                    email.setError("email incorrect!");
                    email.requestFocus();
                    return;
                }
                if (txtPassword.isEmpty()){
                    password.setError("mot de passe incorrect!");
                    password.requestFocus();
                    return;
                }
                if (txtPassword.length() < 6){
                    password.setError("choisi un ùot de passe de 6 caractere au minimum!");
                    password.requestFocus();
                    return;
                }




                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Utilisateur utilisateur=new Utilisateur(txtName,txtPrenom,txtEmail,txtPhone,txtPassword,txtFiliere);

                            FirebaseDatabase.getInstance().getReference("Utilisateurs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(utilisateur)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,"Bien enregistré",Toast.LENGTH_LONG).show();
                                                Intent loginIntent= new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(loginIntent);
                                                finish();
                                            }else{
                                                Toast.makeText(RegisterActivity.this,"registration echoué",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}