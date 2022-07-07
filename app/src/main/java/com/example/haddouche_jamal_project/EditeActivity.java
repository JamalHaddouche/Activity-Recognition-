package com.example.haddouche_jamal_project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EditeActivity extends AppCompatActivity {

    TextView name,prenom,phone,email,filiere;
    Button acceuilBtn;

    private TextInputLayout nameEditText, emailEditText, prenomEditText, phoneEditText, passwordEditText;

    //Image edite
    ImageView image;
    Button editePhotoBtn,saveBtn;

    String userId;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;
    private FirebaseFirestore fStore;
    StorageReference storageReference;
    String currentPhotoPath;
    ActivityResultLauncher<Intent> a;
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;


    private static final int CAMERA_PERMISSION_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite);


        image=findViewById(R.id.imageEdite);
        editePhotoBtn=findViewById(R.id.changePhoto);
        saveBtn=findViewById(R.id.editeBtn);

        nameEditText = findViewById(R.id.nameEdite);
        prenomEditText = findViewById(R.id.prenomEdite);
        emailEditText = findViewById(R.id.emailEdite);
        phoneEditText = findViewById(R.id.phoneEdite);
        filiere=findViewById(R.id.filiere_text);
        passwordEditText = findViewById(R.id.passwordEdite);


        name=findViewById(R.id.nom1);
        prenom=findViewById(R.id.prenom1);
        phone=findViewById(R.id.phone_text);
        email=findViewById(R.id.email_text);
        acceuilBtn=findViewById(R.id.acceuil);

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("utilisateurs/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(image);
            }
        });




        reference= FirebaseDatabase.getInstance().getReference("Utilisateurs");
        userId=user.getUid();
        //récuperer le nom et prenom d'utilisateur
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilisateur utilisateur=snapshot.getValue(Utilisateur.class);

                if(utilisateur!=null){
                    String txtName=utilisateur.getName();
                    String txtPrenom=utilisateur.getPrenom();
                    String txtPhone=utilisateur.getPhone();
                    String txtEmail=utilisateur.getEmail();
                    String txtFiliere=utilisateur.getFiliere();



                    name.setText(txtName);
                    prenom.setText(txtPrenom);
                    phone.setText(txtPhone);
                    email.setText(txtEmail);
                    filiere.setText(txtFiliere);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditeActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        editePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PopupMenu popupMenu = new PopupMenu(EditeActivity.this,editePhotoBtn);
                popupMenu.getMenuInflater().inflate(R.menu.change_image_popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener((item)->{
                    int id = item.getItemId();
                    switch (id){
                        case R.id.first:
                            changeImageFromCamera();
                            break;
                        case R.id.second:
                            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                            break;
                    }
                    return true;
                });
                popupMenu.show();

            }
        });


        //changer les autres informations
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile();
            }
        });




        //revenir à la page d'acceuil
        acceuilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent acceuil=new Intent(EditeActivity.this,HomeActivity.class);
                startActivity(acceuil);
                finish();
            }
        });
        a = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent i = result.getData();
                    if (i == null)
                        return;
                    Bundle extras = i.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    if (bitmap != null) {
                        uploadImageToFirebase(getImageUri(bitmap));
                        image.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(this, "fichier non trouvé", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void changeImageFromCamera(){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA
            },CAMERA_PERMISSION_CODE);
            Toast.makeText(this, "Geting camera permission", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        a.launch(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri =  data.getData();
                uploadImageToFirebase(imageUri);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("utilisateurs/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(image);
                        Toast.makeText(EditeActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
    }


    private void changeProfile() {
        String edName = nameEditText.getEditText().getText().toString().trim();
        String edPrenom = prenomEditText.getEditText().getText().toString().trim();
        String edemail = emailEditText.getEditText().getText().toString().trim();
        String edphone = phoneEditText.getEditText().getText().toString().trim();
        String edPassword = passwordEditText.getEditText().getText().toString().trim();


        if (edName.isEmpty()){
            nameEditText.setError("Full Name is required!");
            nameEditText.requestFocus();
            return;
        }
        if (edPrenom.isEmpty()){
            prenomEditText.setError("Full Name is required!");
            nameEditText.requestFocus();
            return;
        }
        if (edphone.isEmpty()){
            phoneEditText.setError("Phone number is required!");
            phoneEditText.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(edphone).matches()){
            phoneEditText.setError("Please provide valid phone number");
            phoneEditText.requestFocus();
            return;
        }
        if (edemail.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edemail).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return;
        }

        if (edPassword.isEmpty()){
            passwordEditText.setError("Full Name is required!");
            passwordEditText.requestFocus();
            return;
        }
        Utilisateur data = new Utilisateur(edName, edPrenom, edemail, edphone, edPassword);
        reference.child(userId).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditeActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EditeActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}