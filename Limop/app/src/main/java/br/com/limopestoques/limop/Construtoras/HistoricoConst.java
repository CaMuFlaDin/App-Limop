package br.com.limopestoques.limop.Construtoras;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aluno on 18/05/2018.
 */

public class HistoricoConst {

    String data, intermediario, qtd, valor;

    public HistoricoConst(String data, String inter, String qtd, String valor){
        this.data = data;
        this.intermediario = inter;
        this.qtd = qtd;
        this.valor = valor;
    }

    public String getData(){
        return this.data;
    }

    public String getInter(){
        return this.intermediario;
    }
    public String getQtd(){
        return this.qtd;
    }
    public String getValor(){
        return this.valor;
    }
}
