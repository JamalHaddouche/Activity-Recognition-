package com.example.haddouche_jamal_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    TextView nom,prenom,phone,email;
    Button editeBtn,logout;

    ImageView homeImage;
    RelativeLayout location,historique,mapBtn;

    String userId;
    FirebaseUser user;
    FirebaseAuth auth;
    StorageReference storageReference;
    private DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeImage=findViewById(R.id.homeImage);
        nom=findViewById(R.id.utilisateurNom);
        prenom=findViewById(R.id.utilisateurPrenom);
        phone=findViewById(R.id.phone_number);
        email=findViewById(R.id.email_profil);
        editeBtn=findViewById(R.id.edite);
        logout=findViewById(R.id.logout);
        location=findViewById(R.id.location);
        historique=findViewById(R.id.historique);
        mapBtn=findViewById(R.id.mapBTN);


        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        reference=FirebaseDatabase.getInstance().getReference("Utilisateurs");
        userId=user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("utilisateurs/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(homeImage);
            }
        });

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilisateur utilisateur=snapshot.getValue(Utilisateur.class);

                if(utilisateur!=null){
                    String txtName=utilisateur.getName();
                    String txtPrenom=utilisateur.getPrenom();
                    String txtPhone=utilisateur.getPhone();
                    String txtEmail=utilisateur.getEmail();

                    nom.setText(txtName);
                    prenom.setText(txtPrenom);
                    phone.setText(txtPhone);
                    email.setText(txtEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        editeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,EditeActivity.class);
                startActivity(intent);
            }
        });

        //déconnexion
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getInstance().signOut();
                Intent logout=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(logout);
                finish();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(HomeActivity.this,LocationActivity.class);
                startActivity(intent);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historique=new Intent(HomeActivity.this,HistoriqueActivity.class);
                startActivity(historique);
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map=new Intent(HomeActivity.this,PolygonActivity.class);
                startActivity(map);
            }
        });
        findViewById(R.id.activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(HomeActivity.this, new  String[]{Manifest.permission.ACTIVITY_RECOGNITION},1);
                return;
                }
                startActivity(new Intent(HomeActivity.this,RecognitionActivity.class));
            }
        });
    }
}