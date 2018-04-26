package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 05/03/2018.
 */

public class ClientesConst {

    String id,nome, tipo, email;

    public ClientesConst(String id, String nome, String tipo, String email){
        this.id = id;
        this.nome = nome;

        this.tipo = tipo;
        this.email = email;
    }

    public String getId() {return this.id; }

    public String getNome(){
        return this.nome;
    }

    public String getTipo(){
        return  this.tipo;
    }

    public String getEmail(){
        return this.email;
    }
}
