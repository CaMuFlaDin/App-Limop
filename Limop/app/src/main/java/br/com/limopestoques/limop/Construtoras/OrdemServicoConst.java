package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 18/05/2018.
 */

public class OrdemServicoConst {

    String id, cliente, equipamento, numPedido, numVenda;

    public OrdemServicoConst(String id, String cliente, String equipamento, String numPedido, String numVenda){
        this.id = id;
        this.cliente = cliente;
        this.equipamento = equipamento;
        this.numPedido = numPedido;
        this.numVenda = numVenda;
    }

    public String getId(){
        return this.id;
    }
    public String getEquipamento(){
        return this.equipamento;
    }

    public String getNumPedido(){
        return this.numPedido;
    }

    public String getNumVenda(){
        return this.numVenda;
    }

    public String getCliente(){
        return this.cliente;
    }
}

