package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 14/05/2018.
 */

public class ServicosVendasConst {

    String id, servico, valor, qtd,nome_cliente;

    public ServicosVendasConst(String id, String serv, String valor, String qtd, String nome_cliente) {
        this.id = id;
        this.servico = serv;
        this.valor = valor;
        this.qtd = qtd;
        this.nome_cliente = nome_cliente;
    }

    public String getId() {
        return this.id;
    }

    public String getProd() {
        return this.servico;
    }

    public String getValor() {
        return this.valor;
    }

    public String getQtd() {
        return this.qtd;
    }
    public String getNome_cliente() {
        return this.nome_cliente;
    }
}
