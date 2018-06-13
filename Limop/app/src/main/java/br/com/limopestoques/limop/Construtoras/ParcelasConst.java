package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 11/06/2018.
 */

public class ParcelasConst {

    String id,vencimento, cliente, valor;

    public ParcelasConst(String id, String cliente, String valor, String vencimento){
        this.id = id;
        this.cliente = cliente;
        this.valor = valor;
        this.vencimento = vencimento;
    }

    public String getId() {return this.id; }

    public String getCliente(){
        return this.cliente;
    }

    public String getValor(){
        return this.valor;
    }

    public String getVencimento(){
        return  this.vencimento;
    }

}
