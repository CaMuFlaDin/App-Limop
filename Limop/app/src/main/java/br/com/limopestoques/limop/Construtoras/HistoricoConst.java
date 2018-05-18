package br.com.limopestoques.limop.Construtoras;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aluno on 18/05/2018.
 */

public class HistoricoConst {

    String data, intermediario, qtd, valor,tipo;

    public HistoricoConst(String data, String inter, String qtd, String valor,String tipo){
        this.data = data;
        this.intermediario = inter;
        this.qtd = qtd;
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getData(){
        SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos2 = new ParsePosition(0);
        Date data2 = formato2.parse(data,pos2);
        formato2 = new SimpleDateFormat("dd/MM/yyyy");
        String date2 = formato2.format(data2);
        return date2;
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
    public String getTipo(){
        return this.tipo;
    }
}
