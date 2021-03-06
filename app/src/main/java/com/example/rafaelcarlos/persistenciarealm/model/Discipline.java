package com.example.rafaelcarlos.persistenciarealm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rafaelcarlos on 08/03/16.
 */
public class Discipline extends RealmObject {
    public static final String ID = "com.example.rafaelcarlos.persistenciarealm.model.RealmObject.ID";

    @PrimaryKey
    private long id;
    private String nome;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
