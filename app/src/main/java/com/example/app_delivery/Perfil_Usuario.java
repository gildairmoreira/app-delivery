package com.example.app_delivery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil_Usuario extends AppCompatActivity {
    private CircleImageView foto_usuario;
    private TextView nome_usuario, email_usuario;
    private Button btnEditarPerfil;
    private String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener((value, error) -> {
            if (value != null) {
                Glide.with(Perfil_Usuario.this).load(value.getString("foto")).into(foto_usuario);
                nome_usuario.setText(value.getString("nome"));
                email_usuario.setText(email);
                btnEditarPerfil.setOnClickListener((view) -> {
                    Intent intent = new Intent(getApplicationContext(), Editar_Perfil.class);
                    startActivity(intent);
                });
            }
        });
    }

    public void IniciarComponentes() {
        foto_usuario = findViewById(R.id.foto_usuario);
        nome_usuario = findViewById(R.id.nome_usuario);
        email_usuario = findViewById(R.id.email_usuario);
        btnEditarPerfil = findViewById(R.id.btn_editarPerfil);
    }
}