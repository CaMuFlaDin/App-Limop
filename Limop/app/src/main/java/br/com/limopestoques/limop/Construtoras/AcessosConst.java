package br.com.limopestoques.limop.Construtoras;

/**
 * Created by User on 04/03/2018.
 */

public class AcessosConst {

    String user, qtd, data;

    public AcessosConst(String user, String qtd, String data){
        this.user = user;
        this.qtd = qtd;
        this.data = data;
    }

    public String getUser(){
        return this.user;
    }

    public String getQtd(){
        return this.qtd;
    }

    public String getData(){
        return this.data;
    }
}
