package com.example.app_delivery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Form_Cadastro extends AppCompatActivity {
    private CircleImageView fotoUsuario;
    private Button btn_selecionarFoto, btn_cadastrar;
    private EditText edit_nome, edit_email, edit_senha;
    private TextView txt_mensagemError;

    private String usuarioID;
    private Uri mSelecionarUri;

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

        btn_selecionarFoto.setOnClickListener(view -> SelecionarFotoGaleria());
    }

    public void CadastrarUsuario(View view) {
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SalvarDadosUsuario();
                Snackbar snackbar = Snackbar.make(view, "Usuário Cadastrado com Sucesso!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> finish());
                snackbar.show();
            } else {
                String erro;
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Digite uma Senha Mais Forte!";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "Digite um E-mail Válido!";
                } catch (FirebaseAuthUserCollisionException e) {
                    erro = "Esta Conta Já Existe!";
                } catch (FirebaseNetworkException e) {
                    erro = "Sem Conexão com a Internet!";
                } catch (Exception e) {
                    erro = "Erro ao Cadastrar Usuário: " + e.getMessage();
                }
                txt_mensagemError.setText(erro);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    mSelecionarUri = data.getData();

                    try {
                        fotoUsuario.setImageURI(mSelecionarUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    public void SelecionarFotoGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    public void SalvarDadosUsuario() {
        String nomeArquivo = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/images/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
                .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String foto = uri.toString();
                            //Iniciar Banco de Dados
                            String nome = edit_nome.getText().toString();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> usuarios = new HashMap<>();
                            usuarios.put("nome", nome);
                            usuarios.put("foto", foto);

                            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
                            documentReference.set(usuarios).addOnSuccessListener(unused -> {
                                Log.i("db", "Sucesso ao Salvar Dados");
                            }).addOnFailureListener(e -> {
                                Log.i("db_error", "Erro ao Salvar Dados" + e.toString());
                            });
                        })
                        .addOnFailureListener(e -> Log.i("url_img", e.getMessage()))
                ).addOnFailureListener(e -> Log.i("url_img", e.getMessage()));
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