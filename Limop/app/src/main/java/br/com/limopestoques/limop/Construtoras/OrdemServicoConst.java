package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 18/05/2018.
 */

public class OrdemServicoConst {

    String id, cliente, equipamento, numPedido;

    //Dados
    public OrdemServicoConst(String id, String cliente, String equipamento, String numPedido){
        this.id = id;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.numPedido = numPedido;
    }

    //Getters
    public String getId(){
        return this.id;
    }

    public String getEquipamento(){
        return this.equipamento;
    }

    public String getNumPedido(){
        return this.numPedido;
    }


    public String getCliente(){
        return this.cliente;
    }
}

