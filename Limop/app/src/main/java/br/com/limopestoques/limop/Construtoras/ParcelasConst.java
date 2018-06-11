package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 11/06/2018.
 */

public class ParcelasConst {

    String id,vencimento, cliente, valor;

    public ParcelasConst(String id, String vencimento, String cliente, String valor){
        this.id = id;
        this.vencimento = vencimento;
        this.cliente = cliente;
        this.valor = valor;
    }

    public String getId() {return this.id; }

    public String getVencimento(){
        return  this.vencimento;
    }

    public String getCliente(){
        return this.cliente;
    }

    public String getValor(){
        return this.valor;
    }

}
