package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 17/05/2018.
 */

public class ProdVendaConst {

    String id,prod, valor, qtd,img, nome_cliente;

    public ProdVendaConst(String id, String prod, String valor, String qtd,String img, String nome_cliente){
        this.id = id;
        this.prod = prod;
        this.valor = valor;
        this.qtd = qtd;
        this.img = img;
        this.nome_cliente = nome_cliente;
    }

    public String getId() {
        return this.id;
    }

    public String getProd(){
        return this.prod;
    }

    public String getValor(){
        return this.valor;
    }

    public String getQtd(){
        return this.qtd;
    }
    public String getImg(){
        return this.img;
    }
    public String getNome_cliente(){
        return  this.nome_cliente;
    }
}
