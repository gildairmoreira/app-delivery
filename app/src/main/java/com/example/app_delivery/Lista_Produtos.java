package com.example.app_delivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Lista_Produtos extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.perfil) {
            Intent intent = new Intent(Lista_Produtos.this, Perfil_Usuario.class);
            startActivity(intent);

            return true;
        } else if (itemId == R.id.pedidos) {
            // Lógica para abrir a tela de pedidos
            return true;
        } else if (itemId == R.id.delogar) {
            Toast.makeText(Lista_Produtos.this, "Usuário Deslogado", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Lista_Produtos.this, Form_Login.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}