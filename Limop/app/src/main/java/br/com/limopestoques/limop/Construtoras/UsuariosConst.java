package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 02/03/2018.
 */

public class UsuariosConst {

    String id,name, email,tipo;

    public UsuariosConst(String id, String name, String email, String tipo){
        this.id = id;
        this.name = name;
        this.email = email;
        this.tipo = tipo;
    }

    public String getId(){
        return  this.id;
    }

    public String getName(){
        return  this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getTipo(){
        return this.tipo;
    }
}