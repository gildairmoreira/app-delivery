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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_delivery.Adapter.AdapterProduto;
import com.example.app_delivery.model.Produto;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Lista_Produtos extends AppCompatActivity {

    private RecyclerView recyclerView_produtos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView_produtos = findViewById(R.id.recyclerView_produtos);
        produtoList = new ArrayList<>();
        adapterProduto = new AdapterProduto(getApplicationContext(), produtoList);
        recyclerView_produtos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_produtos.setHasFixedSize(true);
        recyclerView_produtos.setAdapter(adapterProduto);

        Produto produto = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto);
        Produto produto1 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto1);
        Produto produto2 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto2);
        Produto produto3 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto3);
        Produto produto4 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto4);
        Produto produto5 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto5);
        Produto produto6 = new Produto("produto 1", "50 real", R.drawable.ic_launcher_background);
        produtoList.add(produto6);
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