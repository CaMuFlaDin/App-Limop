package br.com.limopestoques.limop.Construtoras;

/**
 * Created by User on 04/03/2018.
 */

public class ProdCompraConst {

    String id,prod, valor, qtd,img;

    public ProdCompraConst(String id, String prod, String valor, String qtd,String img){
        this.id = id;
        this.prod = prod;
        this.valor = valor;
        this.qtd = qtd;
        this.img = img;
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
}
