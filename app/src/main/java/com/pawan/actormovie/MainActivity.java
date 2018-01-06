package com.pawan.actormovie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    Button btnSave;
    Spinner spnMovies;
    ListView lvAcor;
    List actList;
    DatabaseReference dbActor;


    @Override
    protected void onStart() {
        super.onStart();
        dbActor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                actList.clear();
                for(DataSnapshot actSnap : dataSnapshot.getChildren())
                {
                    Actor act =actSnap.getValue(Actor.class);
                    actList.add(act);
                }

                ActorList adapter = new ActorList(MainActivity.this,actList);
                lvAcor.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm=(ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nii=cm.getActiveNetworkInfo();

        dbActor = FirebaseDatabase.getInstance().getReference("actor");
        lvAcor = (ListView) findViewById(R.id.lvActor);
        btnSave = (Button) findViewById(R.id.btnSave);
        spnMovies = (Spinner) findViewById(R.id.spnMovies);
        etName = (EditText) findViewById(R.id.etName);
        actList = new ArrayList<>();
        if(nii!=null){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActor();
            }
        });
        lvAcor.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Actor ac = (Actor) actList.get(i);
                showADialog(ac.getaid(),ac.getaName());

                return false;
            }
        });
    }
        else
        {
            Toast.makeText(this, "turn On the internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void addActor() {

        String name = etName.getText().toString().trim();
        String movie = spnMovies.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name))
        {
        String aid = dbActor.push().getKey();
        Actor act = new Actor(name,aid,movie);
        dbActor.child(aid).setValue(act);
        }
        else
        {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
        }
    }

        private void showADialog(final String AId , String acname){
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            LayoutInflater inf = getLayoutInflater();
            final View dialogView = inf.inflate(R.layout.alert_d,null);
            ad.setView(dialogView);

            final EditText etNamee = (EditText) dialogView.findViewById(R.id.etNameD);
            final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
            final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
            final Spinner spnMovies = (Spinner) dialogView.findViewById(R.id.spnMoviess);
            ad.setTitle("Updating Deleting Actor!!"+acname);

            final AlertDialog addd = ad.create();
            addd.show();

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nm = etNamee.getText().toString().trim();
                    String mv = spnMovies.getSelectedItem().toString();

                    if(TextUtils.isEmpty(nm))
                    {
                    etNamee.setError("name Reqiured");
                        return;
                    }
                updateA(AId,nm,mv);
                addd.dismiss();
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                String nm = etNamee.getText().toString().trim();
                deleteAct(AId,nm);
                addd.dismiss();
                }
            });
        }

    private void deleteAct(String aId, String name) {
        DatabaseReference dele = FirebaseDatabase.getInstance().getReference("actor").child(aId);

        dele.removeValue();
        Toast.makeText(this, "Atrist deleted"+name, Toast.LENGTH_SHORT).show();
    }


    private boolean updateA(String Id , String Name, String film){
            DatabaseReference updatere = FirebaseDatabase.getInstance().getReference("actor").child(Id);
            Actor newAc = new Actor(Name,Id,film);

            updatere.setValue(newAc);
            Toast.makeText(this, "Artist Updated "+Name, Toast.LENGTH_SHORT).show();
            return  true;
        }

}
