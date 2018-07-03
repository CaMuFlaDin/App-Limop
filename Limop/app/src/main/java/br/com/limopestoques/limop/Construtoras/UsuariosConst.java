package br.com.limopestoques.limop.Construtoras;

/**
 * Created by Aluno on 02/03/2018.
 */

public class UsuariosConst {

    String id,name, email,tipo,imagem;

    //Dados
    public UsuariosConst(String id, String name, String email, String tipo,String img){
        this.id = id;
        this.name = name;
        this.email = email;
        this.tipo = tipo;
        this.imagem = img;
    }

    //Getters
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
    public String getImg(){
        return this.imagem;
    }
}