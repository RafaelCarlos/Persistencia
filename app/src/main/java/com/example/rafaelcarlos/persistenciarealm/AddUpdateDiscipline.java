package com.example.rafaelcarlos.persistenciarealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafaelcarlos.persistenciarealm.model.Discipline;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddUpdateDiscipline extends AppCompatActivity {

    private Discipline discipline;
    private EditText etName;
    private Button btAdd;
    private Realm realm;
    private RealmResults<Discipline> disciplines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_discipline);

        etName = (EditText) findViewById(R.id.et_name_activity_add);
        btAdd = (Button) findViewById(R.id.bt_add_activity_add);
        TextView title = (TextView) findViewById(R.id.tv_title);

        discipline = new Discipline();

        realm = Realm.getDefaultInstance();
        disciplines = realm.where(Discipline.class).findAll();

        //Se for maior que zero significa que um update.
        if (getIntent() != null && getIntent().getLongExtra(Discipline.ID, 0) > 0) {

            discipline.setId(getIntent().getLongExtra(Discipline.ID, 0));

            discipline = disciplines.where().equalTo("id", discipline.getId()).findAll().get(0);


            etName.setText(discipline.getNome());

            title.setText("Atualizar disciplina");
            btAdd.setText("Update");

        }


        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = "atualizada";
                //Significa que é um novo.
                if (discipline.getId() == 0) {
                    //Pegando o ultimo id.
                    disciplines.sort("id", RealmResults.SORT_ORDER_DESCENDING);
                    long id = disciplines.size() == 0 ? 1 : disciplines.get(0).getId() + 1;
                    discipline.setId(id);
                    label = "adicionada";
                }

                try {

                    realm.beginTransaction();
                    discipline.setNome(etName.getText().toString());
                    realm.copyToRealm(discipline);
                    realm.commitTransaction();

                    Toast.makeText(AddUpdateDiscipline.this, "Disciplina " + label, Toast.LENGTH_LONG).show();
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddUpdateDiscipline.this, "Falhou", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
