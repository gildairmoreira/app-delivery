package com.example.app_delivery;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Detalhes_Produto extends AppCompatActivity {
    private ImageView dt_fotoProduto;
    private TextView dt_nomeProduto;
    private TextView dt_precoProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        IniciarComponentes();

        String foto = getIntent().getExtras().getString("foto");
        String nome = getIntent().getExtras().getString("nome");
        String preco = getIntent().getExtras().getString("preco");

        Glide.with(this).load(foto).into(dt_fotoProduto);
        dt_nomeProduto.setText(nome);
        dt_precoProduto.setText(preco);

    }
    public void IniciarComponentes(){
        dt_fotoProduto = findViewById(R.id.dt_fotoProduto);
        dt_nomeProduto = findViewById(R.id.dt_nomeProduto);
        dt_precoProduto = findViewById(R.id.dt_precoProduto);

    }
}