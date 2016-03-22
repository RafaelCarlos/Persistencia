package com.example.rafaelcarlos.persistenciarealm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.rafaelcarlos.persistenciarealm.model.Discipline;
import com.example.rafaelcarlos.persistenciarealm.model.Student;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Button btDisciplina, btAluno;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btDisciplina = (Button) findViewById(R.id.bt_disciplinas);
        btAluno = (Button) findViewById(R.id.bt_alunos);

        realm = Realm.getDefaultInstance();

        RealmResults<Discipline> disciplines = realm.where(Discipline.class).findAll();
        RealmResults<Student> students = realm.where(Student.class).findAll();

        btDisciplina.setText("Disciplinas (" + disciplines.size() + ") ");
        btAluno.setText("Estudantes (" + students.size() + ") ");
        realm.close();

        btDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DisciplinaActivity.class));
            }
        });

        btAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentsActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
