package com.example.rafaelcarlos.persistenciarealm.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rafaelcarlos on 08/03/16.
 */
public class Student extends RealmObject {

    public static String ID = "com.example.rafaelcarlos.persistenciarealm.model.RealmObject.ID";

    @PrimaryKey
    private long id;
    private String nome;
    private String email;
    private RealmList<Nota> notas;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome == null ? "" : nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(RealmList<Nota> notas) {
        this.notas = notas;
    }
}
