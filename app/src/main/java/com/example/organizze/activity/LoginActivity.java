package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoEntrar = findViewById(R.id.buttonEntrar);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if (!textoEmail.isEmpty()){
                    if (!textoSenha.isEmpty()){
                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        validarLogin();

                    }else{
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,
                            "Sucesso ao fazer login",
                            Toast.LENGTH_SHORT).show();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta de e-mail já foi cadastrada!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário! " + e.getMessage();
                        //printStackTrace() serve pra printar a exceção no Log
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            "Erro ao fazer login!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}