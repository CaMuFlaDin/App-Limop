package com.estoques.limop.limop.Construtoras;

/**
 * Created by User on 04/03/2018.
 */

public class ServicosCompraConst {

    private String id ,nome, custo, venda;

    public ServicosCompraConst(String id,String nome, String custo, String venda){
        this.id = id;
        this.nome = nome;
        this.custo = custo;
        this.venda = venda;
    }

    public String getId() {
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCusto(){
        return this.custo;
    }

    public String getVenda(){
        return this.venda;
    }
}
