package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 14/05/2018.
 */

public class ServicosVendasConst {

    String id, prod, valor, qtd;

    public ServicosVendasConst(String id, String prod, String valor, String qtd) {
        this.id = id;
        this.prod = prod;
        this.valor = valor;
        this.qtd = qtd;
    }

    public String getId() {
        return this.id;
    }

    public String getProd() {
        return this.prod;
    }

    public String getValor() {
        return this.valor;
    }

    public String getQtd() {
        return this.qtd;
    }
}
