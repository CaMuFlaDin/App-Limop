package br.com.limopestoques.limop;

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

    public void Produtos(View v){
        Intent irTela = new Intent(Vendas.this, Produtos_Vendas.class);
        startActivity(irTela);
    }

    public void Servicos(View v){
        Intent irTela = new Intent(Vendas.this, Servicos_Vendas.class);
        startActivity(irTela);
    }

    public void OrdemServico(View v){
        Intent irTela = new Intent(Vendas.this, OrdemServico.class);
        startActivity(irTela);
    }

    public void Graficos(View v){
        Intent irTela = new Intent(Vendas.this, Graficos.class);
        startActivity(irTela);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Vendas.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }

}
