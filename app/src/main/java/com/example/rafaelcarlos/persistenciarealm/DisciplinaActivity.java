package com.example.rafaelcarlos.persistenciarealm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.rafaelcarlos.persistenciarealm.adapter.DisciplineAdapter;
import com.example.rafaelcarlos.persistenciarealm.model.Discipline;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class DisciplinaActivity extends AppCompatActivity {

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmResults<Discipline> disciplines;
    private ListView lvDisciplinas;
    private Button addDisciplinas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        lvDisciplinas = (ListView) findViewById(R.id.lv_disciplinas);
        addDisciplinas = (Button) findViewById(R.id.bt_add_disciplinas);


        realm = Realm.getDefaultInstance();

        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                ((DisciplineAdapter) lvDisciplinas.getAdapter()).notifyDataSetChanged();
            }
        };

        realm.addChangeListener(realmChangeListener);

        disciplines = realm.where(Discipline.class).findAll();
        lvDisciplinas.setAdapter(new DisciplineAdapter(this, realm, disciplines, false));

        addDisciplinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisciplinaActivity.this, AddUpdateDiscipline.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        realm.removeAllChangeListeners();
        realm.close();
        super.onDestroy();
    }
}
