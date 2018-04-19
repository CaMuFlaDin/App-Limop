package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void Vendas(View v){
        Intent irTela = new Intent(Principal.this, Vendas.class);
        startActivity(irTela);
    }

    public void Compras(View v){
        Intent irTela = new Intent(Principal.this, Compras.class);
        startActivity(irTela);
    }

    public void Usuarios(View v){
        Intent irTela = new Intent(Principal.this, Usuarios.class);
        startActivity(irTela);
    }

    public void Acessos(View v){
        Intent irTela = new Intent(Principal.this, Acessos.class);
        startActivity(irTela);
    }

    // TODO FAZER ONBACK
}
