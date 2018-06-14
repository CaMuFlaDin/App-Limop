package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 11/06/2018.
 */

public class ParcelasConst {

    String id,vencimento, cliente, valor, quantidade, id_produto, recebido;

    public ParcelasConst(String id, String cliente, String valor, String vencimento,String quantidade, String id_produto, String recebido){
        this.id = id;
        this.cliente = cliente;
        this.valor = valor;
        this.vencimento = vencimento;
        this.quantidade = quantidade;
        this.id_produto = id_produto;
        this.recebido = recebido;
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

    public String getQuantidade(){
        return this.quantidade;
    }

    public String getId_produto(){
        return this.id_produto;
    }

    public String getRecebido(){
        return this.recebido;
    }

}
