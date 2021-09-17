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
import com.example.androidfirebaseproject.Model.UserModel;
import com.example.androidfirebaseproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class Register extends AppCompatActivity {

    private EditText edt_email_register,
            edt_senha_register,
            edt_nome_register,
            edt_sobrenome_register,
            edt_confirmar_senha_register;

    private Button btn_registrar_register,
            btn_login_register;

    private CheckBox ckb_mostrar_senha_register;
    private ProgressBar loginProgressBar_register;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Instancia o firebase auth
        mAuth = FirebaseAuth.getInstance();

        // Associa as variaveis aos componentes
        edt_nome_register = findViewById(R.id.edt_nome_register);
        edt_sobrenome_register = findViewById(R.id.edt_sobrenome_register);
        edt_email_register = findViewById(R.id.edt_email_register);
        edt_senha_register = findViewById(R.id.edt_senha_register);
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register);
        btn_registrar_register = findViewById(R.id.btn_registrar_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        ckb_mostrar_senha_register = findViewById(R.id.ckb_mostrar_senha_register);
        loginProgressBar_register = findViewById(R.id.loginProgressBar_register);

        // Exibe os caracteres digitados nos campos de senha
        ckb_mostrar_senha_register.setOnCheckedChangeListener((buttonView, isChecked) -> {                                    // Retorna um booleano que corresponde ao feedback da check box
            if (isChecked) {                                                                                             // Se a caixa foi marcada
                edt_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());              // Remove a mascara do texto da senha
                edt_confirmar_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());    // Remove a mascara do texto da senha
            } else {
                edt_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());                 // Volta para a mascara de senha
                edt_confirmar_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());       // Volta para a mascara de senha
            }
        });

        // Realiza o cadastro do usuário com e-mail e senha juntamente ao banco de dados em tempo real
        btn_registrar_register.setOnClickListener(v -> {

            UserModel userModel = new UserModel();      // Inicia a instancia com o construtor vazio

            userModel.setEmail(edt_email_register.getText().toString());
            userModel.setNome(edt_nome_register.getText().toString());
            userModel.setSobrenome(edt_sobrenome_register.getText().toString());
            String senha = edt_senha_register.getText().toString();
            String confirmarSenha = edt_confirmar_senha_register.getText().toString();

            // Verifica se os campos não estão vazios
            if (!TextUtils.isEmpty(userModel.getNome())
                    && !TextUtils.isEmpty(userModel.getSobrenome())
                    && !TextUtils.isEmpty(userModel.getEmail())
                    && !TextUtils.isEmpty(senha)
                    && !TextUtils.isEmpty(confirmarSenha)) {

                // Verifica se a senhas digitadas em ambos os campos são identicas
                if (senha.equals(confirmarSenha)) {
                    loginProgressBar_register.setVisibility(View.VISIBLE);          // Torna a barra de progresso visivel

                    // Cria um usuário com os valores contidos em registerEmail e senha
                    mAuth.createUserWithEmailAndPassword(userModel.getEmail(), senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {                                // Se o login foi im sucesso
                                userModel.setId(mAuth.getUid());                    // Captura o ID do usuario
                                userModel.salvar();                                 // Salva no banco de dados como um novo nó
                                Toast.makeText(Register.this, "Cadastro Realizado com Sucesso!", Toast.LENGTH_SHORT).show();
                                abrirTelaPrincipal();                               // Abre a tela principa;
                            } else {                                                  // Se o login não foi um sucesso
                                String error;
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    error = "A senha deve conter no mínimo 6 caracteres";

                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    error = "E-mail inválido";
                                } catch (FirebaseAuthUserCollisionException e) {
                                    error = "Este e-mail já esta cadastrado";
                                } catch (Exception e) {
                                    error = "Erro ao efetuar cadastro";
                                    e.printStackTrace();
                                }
                                Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();

                                error = task.getException().getMessage();                                                   // Armazena a mensagem de erro em error
                                Toast.makeText(Register.this, "" + error, Toast.LENGTH_SHORT).show();           // Exibe a mensagem de erro
                            }
                            loginProgressBar_register.setVisibility(View.INVISIBLE);                                        // Torna a barra de progresso invisivel
                        }
                    });
                } else {   // Se as senhas nos campos senha e redigite a senha não forem iguais
                    Toast.makeText(Register.this, "As senhas devem ser a mesma em ambos os campos!", Toast.LENGTH_SHORT).show();    // Exibe a mensagem de erro
                }

            } else {
                Toast.makeText(Register.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }

        });

        // Redireciona para a tela de login (LoginActivity)
        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Redireciona para tela principal (MainActivity)
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}