package com.example.androidfirebaseproject.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserModel {
    private String id,
                 nome,
            sobrenome,
                email;


    public UserModel() {
    }


    public UserModel(String id, String nome, String sobrenome, String email) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void salvar(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(); // inicia a instancia do firebase e a referencia, que seria o caminho que vai ser criado
        reference.child("usuarios").child(getId()).setValue(this);                   // o child é o nó, então estamos criando um nó chamado usuários
    }
}
