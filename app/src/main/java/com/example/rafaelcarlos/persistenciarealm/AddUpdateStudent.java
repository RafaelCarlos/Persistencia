package com.example.rafaelcarlos.persistenciarealm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafaelcarlos.persistenciarealm.model.Discipline;
import com.example.rafaelcarlos.persistenciarealm.model.Nota;
import com.example.rafaelcarlos.persistenciarealm.model.Student;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddUpdateStudent extends AppCompatActivity {

    private Realm realm;
    private RealmResults<Student> students;
    private RealmResults<Discipline> disciplines;

    private Student student;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_student);

        student = new Student();
        etName = (EditText) findViewById(R.id.et_name);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        Button btAddUpdate = (Button) findViewById(R.id.bt_add_update);

        realm = Realm.getDefaultInstance();
        students = realm.where(Student.class).findAll();

        if (getIntent() != null && getIntent().getLongExtra(Student.ID, 0) > 0) {
            student.setId(getIntent().getLongExtra(Student.ID, 0));

            student = students.where().equalTo("id", student.getId()).findAll().get(0);
            etName.setText(student.getNome());
            tvTitle.setText("Atualizar disciplina");
            btAddUpdate.setText("Update");
        }


        disciplines = realm.where(Discipline.class).findAll();


        // FIRST BLOCK
        Spinner spDiscipline = (Spinner) findViewById(R.id.sp_discipline);
        spDiscipline.setAdapter(new DisciplineSpinnerAdapter(this, disciplines));//TODO

        View btRemove = findViewById(R.id.bt_remove);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveGrade(v);
            }
        });


        // UPDATE
        if (student.getNotas() != null) {
            for (Nota g : student.getNotas()) {
                createGradeForView(findViewById(R.id.bt_add), disciplines, g);
            }
            callRemoveGrade(btRemove);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // LISTENERS
    public void callAddUpdateStudent(View view) {
        String label = "atualizado";
        if (student.getId() == 0) {
            students.sort("id", RealmResults.SORT_ORDER_DESCENDING);
            long id = students.size() == 0 ? 1 : students.get(0).getId() + 1;
            student.setId(id);
            label = "adicionado";
        }

        try {
            realm.beginTransaction();
            student.setNome(etName.getText().toString());
            realm.copyToRealmOrUpdate(student);
            realm.commitTransaction();

            student = realm.where(Student.class).equalTo("id", student.getId()).findFirst();

            realm.beginTransaction();
            student.getNotas().clear();
            student.getNotas().addAll(getGradesFromView(view, disciplines));
            realm.commitTransaction();

            Toast.makeText(AddUpdateStudent.this, "Estudante " + label, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddUpdateStudent.this, "Falhou!", Toast.LENGTH_SHORT).show();
        }
    }

    public void callAddGrade(View view) {
        LinearLayout llParent = (LinearLayout) view.getParent();

        if (llParent.getChildCount() - 1 == disciplines.size()) {
            Toast.makeText(this, "Máximo de disciplinas atingido", Toast.LENGTH_SHORT).show();
            return;
        }

        createGradeForView(view, disciplines, null);
    }

    private void callRemoveGrade(View view) {
        LinearLayout llParent = (LinearLayout) view.getParent().getParent();

        if (llParent.getChildCount() > 2) {
            llParent.removeView((LinearLayout) view.getParent());
        }
    }


    // UTIL
    private void createGradeForView(View view, RealmResults<Discipline> disciplines, Nota nota) {
        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout llChild = (LinearLayout) inflater.inflate(R.layout.box_discipline_grade, null);

        Spinner spDiscipline = (Spinner) llChild.findViewById(R.id.sp_discipline);
        spDiscipline.setAdapter(new DisciplineSpinnerAdapter(this, disciplines));//TODO
        if (nota != null) {
            spDiscipline.setSelection(getDisciplinePosition(disciplines, nota.getDiscipline()));
        }

        EditText etGrade = (EditText) llChild.findViewById(R.id.et_grade);
        if (nota != null) {
            etGrade.setText(String.valueOf(nota.getGrade()));
        }

        View btRemove = llChild.findViewById(R.id.bt_remove);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRemoveGrade(v);
            }
        });

        float scale = getResources().getDisplayMetrics().density;
        int margin = (int) (5 * scale + 0.5f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        llChild.setLayoutParams(layoutParams);

        LinearLayout llParent = (LinearLayout) view.getParent();
        llParent.addView(llChild, llParent.getChildCount() - 1);
    }

    private int getDisciplinePosition(RealmResults<Discipline> disciplines, Discipline discipline) {
        for (int i = 0; i < disciplines.size(); i++) {
            if (disciplines.get(i).getId() == discipline.getId()) {
                return (i);
            }
        }
        return (0);
    }

    private List<Nota> getGradesFromView(View view, RealmResults<Discipline> disciplines) {
        List<Nota> list = new LinkedList<>();
        RelativeLayout rlParent = (RelativeLayout) view.getParent();

        for (int i = 0; i < rlParent.getChildCount(); i++) {

            if (rlParent.getChildAt(i) instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) rlParent.getChildAt(i);
                LinearLayout llChild = (LinearLayout) scrollView.getChildAt(0);

                for (int j = 0; j < llChild.getChildCount(); j++) {
                    if (llChild.getChildAt(j) instanceof LinearLayout) {

                        Spinner spDiscipline = (Spinner) llChild.getChildAt(j).findViewById(R.id.sp_discipline);
                        EditText etGrade = (EditText) llChild.getChildAt(j).findViewById(R.id.et_grade);

                        Nota g = new Nota();
                        if (realm.where(Nota.class).findAll().size() > 0) {
                            g.setId(realm.where(Nota.class).findAll().where().findAllSorted("id", RealmResults.SORT_ORDER_DESCENDING).get(0).getId() + 1 + j);
                        } else {
                            g.setId(1);
                        }

                        Discipline d = new Discipline();
                        d.setId(disciplines.get(spDiscipline.getSelectedItemPosition()).getId());
                        d.setNome(disciplines.get(spDiscipline.getSelectedItemPosition()).getNome());
                        g.setDiscipline(d);

                        g.setGrade(Double.parseDouble(etGrade.getText().toString()));

                        list.add(g);
                    }
                }
            }
        }
        return (list);
    }


}
