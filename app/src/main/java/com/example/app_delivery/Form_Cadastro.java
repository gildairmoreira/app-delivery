package com.example.app_delivery;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_Cadastro extends AppCompatActivity {
    private CircleImageView fotoUsuario;
    private Button btn_selecionarFoto, btn_cadastrar;
    private EditText edit_nome, edit_email, edit_senha;
    private TextView txt_mensagemError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
        edit_nome.addTextChangedListener(cadastroTextWatcher);
        edit_email.addTextChangedListener(cadastroTextWatcher);
        edit_senha.addTextChangedListener(cadastroTextWatcher);

        btn_cadastrar.setOnClickListener(this::CadastrarUsuario);
    }

    public void CadastrarUsuario(View view) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Snackbar snackbar = Snackbar.make(view, "Usuário Cadastrado com Sucesso!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> finish());
                snackbar.show();
            }
        });
    }


    public void IniciarComponentes() {
        fotoUsuario = findViewById(R.id.fotoUsuario);
        btn_selecionarFoto = findViewById(R.id.btn_selecionarFoto);
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        txt_mensagemError = findViewById(R.id.txt_mensagemError);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
    }

    TextWatcher cadastroTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {
                btn_cadastrar.setEnabled(true);
                btn_cadastrar.setBackgroundColor(getResources().getColor(R.color.dark_red));
            } else {
                btn_cadastrar.setEnabled(false);
                btn_cadastrar.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}