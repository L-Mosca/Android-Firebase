package com.example.androidfirebaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidfirebaseproject.Activity.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private Button btn_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();                                                 // Inicia a firebase auth
        btn_logout = findViewById(R.id.btn_logout);                                         // Associa o botão btn_logout com o componente btn_logout


        // Quando clicar no btn_logout
        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();                                                            // Faz o logout do usuario

            Intent intent = new Intent(MainActivity.this, Login.class);    // Processo para redirecionar para tela de login
            startActivity(intent);                                                      // Redireciona para tela de login
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();

        // Define que caso o usuário não esteja logado, será redirecionado para tela de login
        // Então se o usuário ja estiver logado, será levado direto para a tela principal (MainActivity)
        if(currentuser == null){                                                            // Se o usuario não estiver logado
            Intent intent = new Intent(MainActivity.this, Login.class);        // Redireciona para tela de login
            startActivity(intent);                                                          // Abre a tela de login
            finish();
        }
    }
}