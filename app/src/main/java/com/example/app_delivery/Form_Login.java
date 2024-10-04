package com.example.app_delivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Form_Login extends AppCompatActivity {

    private TextView txt_criarConta, txt_mensagemError;
    private EditText edit_email, edit_senha;
    private Button btn_entrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
        txt_criarConta.setOnClickListener(view -> {
            Intent intent = new Intent(Form_Login.this, Form_Cadastro.class);
            startActivity(intent);
        });
        btn_entrar.setOnClickListener(view -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            if (email.isEmpty() || senha.isEmpty()) {
                txt_mensagemError.setText("Preencha todos os campos!");
            } else {
                txt_mensagemError.setText("");
                AutenticarUsuario();
            }
        });


    }

    public void AutenticarUsuario() {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    IniciarTelaProdutos();
                }, 3000);
                btn_entrar.setVisibility(View.INVISIBLE);
            } else {
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e) {
                    erro = "Erro ao Logar";
                }
                txt_mensagemError.setText(erro);
            }
        });
    }

    public void IniciarTelaProdutos() {
        Intent intent = new Intent(Form_Login.this, Lista_Produtos.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) {
            IniciarTelaProdutos();
        }
    }

    public void IniciarComponentes() {
        txt_criarConta = findViewById(R.id.txt_criarConta);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        btn_entrar = findViewById(R.id.btn_entrar);
        progressBar = findViewById(R.id.progressBar);
        txt_mensagemError = findViewById(R.id.txt_mensagemError);

    }
}