package com.example.haddouche_jamal_project;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    ListView listeView;
    ArrayList<Localisation> list;


    String userId;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    private class ListAdapter extends BaseAdapter{
        List<Localisation> list;
        LayoutInflater vi;
        Context context;
        ListAdapter(Context context,List<Localisation> activities){

            this.context = context;
            this.list = activities;
            this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view ==null){
                view = vi.inflate(R.layout.list_item,null);
                //view = getLayoutInflater().inflate(R.layout.list_item,viewGroup,false);
            }
            Localisation localisation = this.list.get(i);
            ((TextView)view.findViewById(R.id.date_historique)).setText("Date: " + localisation.getDate().toString());
            ((TextView)view.findViewById(R.id.pays_historique)).setText("Pays: " + localisation.getPays());
            ((TextView)view.findViewById(R.id.ville_historique)).setText("Ville: " + localisation.getVille());
            ((TextView)view.findViewById(R.id.addresse_historique)).setText("Adresse: " + localisation.getAdresse());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        listeView=findViewById(R.id.liste_view);

        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference("Locations");
        list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) childSnapshot.getValue();
                    String idUser = (String) map.get("idUser");
                    if(idUser.equals(userId)){
                        HashMap<String,Integer> dd = (HashMap<String, Integer>) map.get("date");
                        int y=Integer.parseInt(String.valueOf(dd.get("year"))),
                                m=Integer.parseInt(String.valueOf(dd.get("month"))),
                                d=Integer.parseInt(String.valueOf(dd.get("day"))),
                                h=Integer.parseInt(String.valueOf(dd.get("hours"))),
                                min=Integer.parseInt(String.valueOf(dd.get("minutes"))),
                                s=Integer.parseInt(String.valueOf(dd.get("seconds")));
                        Date date = new Date(y,m,d,h,min,s);
                        String pays = (String) map.get("pays");
                        String ville = (String) map.get("ville");
                        String adresse = (String) map.get("adresse");
                        double alltitude = Double.parseDouble(String.valueOf(map.get("alltitude")));
                        list.add(new Localisation(date, pays, ville, adresse, alltitude, idUser));
                    }
                }
                listeView.setAdapter(new ListAdapter(HistoriqueActivity.this,list));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        }
}