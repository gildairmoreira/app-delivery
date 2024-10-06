package com.example.app_delivery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Editar_Perfil extends AppCompatActivity {

    private CircleImageView foto_usuario;
    private EditText editar_nome;
    private Button btn_selecionarFoto, btn_atualizarDados;
    private Uri mSelecionarUri;
    private String usuarioID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
        btn_selecionarFoto.setOnClickListener(view -> SelecionarFotoGaleria());
        btn_atualizarDados.setOnClickListener(view -> {
            String nome = editar_nome.getText().toString();
            if (nome.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
               AtualizarDados(view);
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
                        foto_usuario.setImageURI(mSelecionarUri);
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

    public void AtualizarDados(View view) {
        String nomeArquivo = UUID.randomUUID().toString();
        final StorageReference reference = FirebaseStorage.getInstance().getReference("/images/" + nomeArquivo);
        reference.putFile(mSelecionarUri)
                .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String foto = uri.toString();
                            //Iniciar Banco de Dados
                            String nome = editar_nome.getText().toString();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> usuarios = new HashMap<>();
                            usuarios.put("nome", nome);
                            usuarios.put("foto", foto);

                            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            db.collection("Usuarios").document(usuarioID)
                                    .update("nome", nome, "foto", foto)
                                    .addOnCompleteListener(task -> {
                                        Snackbar snackbar = Snackbar.make(view, "Dados Atualizados com Sucesso!", Snackbar.LENGTH_SHORT)
                                                .setAction("OK", v -> finish());
                                        snackbar.show();
                                    }).addOnFailureListener(e -> {

                                    });


                        })
                        .addOnFailureListener(e -> Log.i("url_img", e.getMessage()))
                ).addOnFailureListener(e -> Log.i("url_img", e.getMessage()));
    }

    public void IniciarComponentes() {
        foto_usuario = findViewById(R.id.editar_fotoUsuario);
        editar_nome = findViewById(R.id.editar_nome);
        btn_atualizarDados = findViewById(R.id.btn_atualizarPerfil);
        btn_selecionarFoto = findViewById(R.id.btn_editarFoto);
    }
}