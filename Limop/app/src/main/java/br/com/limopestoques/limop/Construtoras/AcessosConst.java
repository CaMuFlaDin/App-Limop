package br.com.limopestoques.limop.Construtoras;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 04/03/2018.
 */

public class AcessosConst {

    String user, qtd, data;

    //Dados
    public AcessosConst(String user, String qtd, String data){
        this.user = user;
        this.qtd = qtd;
        this.data = data;
    }

    //Getters
    public String getUser(){
        return this.user;
    }

    public String getQtd(){
        return this.qtd;
    }

    //Get com Data Formatada
    public String getData(){
        SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos2 = new ParsePosition(0);
        Date data2 = formato2.parse(data,pos2);
        formato2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date2 = formato2.format(data2);
        return date2;
    }
}
