package br.com.limopestoques.limop.Construtoras;

/**
 * Created by User on 04/03/2018.
 */

public class TranspCompraConst {

    private String id, nome, email, valor;

    //Dados
    public TranspCompraConst(String id, String nome, String email, String valor){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.valor = valor;
    }

    //Getters
    public String getId() {
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getEmail(){
        return this.email;
    }

    public String getValor(){
        return this.valor;
    }
}
