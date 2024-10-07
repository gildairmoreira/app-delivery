package com.example.app_delivery.model;

public class Produto {
    private String nome;
    private String preco;
    private int foto;
    private String descricao;

    public Produto(String nome, String preco, int foto) {
        this.nome = nome;
        this.preco = preco;
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
