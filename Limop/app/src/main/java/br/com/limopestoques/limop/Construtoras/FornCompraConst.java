package br.com.limopestoques.limop.Construtoras;

/**
 * Created by User on 04/03/2018.
 */

public class FornCompraConst {

    private String id, nome, tipo, tel;

    //Dados
    public FornCompraConst(String id, String nome, String tipo, String tel){
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.tel = tel;
    }

    //Getters
    public String getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getTipo(){
        return this.tipo;
    }

    public String getTel(){
        return this.tel;
    }
}
