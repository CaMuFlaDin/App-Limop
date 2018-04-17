package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Vendas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas);
    }

    public void Clientes(View v){
        Intent irTela = new Intent(Vendas.this, Clientes.class);
        startActivity(irTela);
    }

    public void Transportadoras(View v){
        Intent irTela = new Intent(Vendas.this, Transportadoras_Compras.class);
        startActivity(irTela);
    }


}
