package com.example.androidfirebaseproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidfirebaseproject.MainActivity;
import com.example.androidfirebaseproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    // Declara tudo que tem na tela de login
    private EditText edt_email,
            edt_senha;
    private Button btn_login,
            btn_registrar;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgressBar;
    private CheckBox ckb_mostrar_senha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instancia o firebase
        mAuth = FirebaseAuth.getInstance();

        // Declara variaveis para assumir os componentes da tela
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        btn_login = findViewById(R.id.btn_login);
        btn_registrar = findViewById(R.id.btn_registrar);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        ckb_mostrar_senha = findViewById(R.id.ckb_mostrar_senha);

        // Realiza a ação de login com a autenticação do firebase
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Captura as entradas de texto do usuário
                String loginEmail = edt_email.getText().toString();                                                     // pega o que foi inserido no edt_email e guarda na string loginEmail
                String loginSenha = edt_senha.getText().toString();                                                     // pega o que foi inserido no edt_senha e guarda na string loginSenha

                // Verifica se os campos não estão vazios.
                // Se um dos campos estiver preenchido
                if (!TextUtils.isEmpty(loginEmail) || !TextUtils.isEmpty(loginSenha)) {

                    loginProgressBar.setVisibility(View.VISIBLE);                                                       // Deixa a barrra de progresso do login visivel
                    mAuth.signInWithEmailAndPassword(loginEmail, loginSenha)                                            // Consulta o firebase com os dados que foram passados pra fazer login
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {                                                            // Se o login der certo
                                    abrirTelaPrincipal();                                                           // Abre a tela principal
                                } else {                                                                              // se der erro no login
                                    String error = task.getException().getMessage();                                // Armazena uma mensagem de erro
                                    Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();     // Mostra a mensagem de erro
                                    loginProgressBar.setVisibility(View.INVISIBLE);                                 // Deixa a barra de progresso invisivel
                                }

                            });

                }

            }
        });

        // Redireciona para a tela de cadastro (Register)
        btn_registrar.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
            finish();
        });

        // Exibe os caracteres da senha digitada
        ckb_mostrar_senha.setOnCheckedChangeListener((buttonView, isChecked) -> {                // Retorna um booleano que corresponde ao feedback da check box
            if (isChecked) {                                                                         // Se a caixa foi marcada
                edt_senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());   // Remove a mascara do texto da senha
            } else {
                edt_senha.setTransformationMethod(PasswordTransformationMethod.getInstance());  // Volta para a mascara de senha
            }
        });
    }

    // Direciona para a tela principal (MainActivity)
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}