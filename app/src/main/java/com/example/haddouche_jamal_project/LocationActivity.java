package com.example.haddouche_jamal_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;


public class LocationActivity extends AppCompatActivity {

    TextView nom, prenom,date,pays,ville,addresse,altitude;
    Button editeBtn, logout;

    ImageView homeImage;

    String userId;
    FirebaseUser user;
    FirebaseAuth auth;
    StorageReference storageReference;
    private DatabaseReference reference,locationReference;


    //Location
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        homeImage = findViewById(R.id.homeImage);
        nom = findViewById(R.id.utilisateurNom);
        prenom = findViewById(R.id.utilisateurPrenom);
        editeBtn = findViewById(R.id.edite);
        logout = findViewById(R.id.logout);

        date=findViewById(R.id.date);
        ville=findViewById(R.id.ville);
        pays=findViewById(R.id.pays);
        addresse=findViewById(R.id.addresse);
        altitude=findViewById(R.id.altitude);


        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        locationReference=FirebaseDatabase.getInstance().getReference().child("Locations");
        reference = FirebaseDatabase.getInstance().getReference("Utilisateurs");

        userId = user.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        StorageReference profileRef = storageReference.child("utilisateurs/" + auth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(homeImage);
            }
        });

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);

                if (utilisateur != null) {
                    String txtName = utilisateur.getName();
                    String txtPrenom = utilisateur.getPrenom();

                    nom.setText(txtName);
                    prenom.setText(txtPrenom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LocationActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        if(ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            showLocation();
        }
        else
            ActivityCompat.requestPermissions(LocationActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);


    }

    @SuppressLint("MissingPermission")
    private void showLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location= task.getResult();
                if(location != null){
                    Geocoder geocoder=new Geocoder(LocationActivity.this, Locale.getDefault());
                    try{
                        List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(),1);
                        double txtAlltitude=location.getAltitude();
                        Date currentTime = Calendar.getInstance().getTime();
                        String txtAddresse=addressList.get(0).getAddressLine(0);
                        String txtVille=addressList.get(0).getLocality();
                        String txtPays=addressList.get(0).getCountryName();

                        Localisation localisation=new Localisation(currentTime,txtPays,txtVille,txtAddresse,txtAlltitude,userId);
                        locationReference.push().setValue(localisation);
                        Toast.makeText(LocationActivity.this,"location enregistré",Toast.LENGTH_SHORT).show();


                        date.setText(""+currentTime);
                        altitude.setText(""+txtAlltitude);
                        addresse.setText("" + addressList.get(0).getAddressLine(0));
                        ville.setText("" +addressList.get(0).getLocality());
                        pays.setText(""+addressList.get(0).getCountryName());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(LocationActivity.this,"Location null error",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}