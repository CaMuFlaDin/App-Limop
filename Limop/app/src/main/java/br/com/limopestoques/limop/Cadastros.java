package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Cadastros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastros);
    }

    //Mudanças de Tela
    public void Clientes(View v){
        Intent irTela = new Intent(Cadastros.this, Clientes.class);
        startActivity(irTela);
    }

    public void Fornecedores(View v){
        Intent irTela = new Intent(Cadastros.this, Fornecedores_compras.class);
        startActivity(irTela);
    }

    public void Transportadoras(View v){
        Intent irTela = new Intent(Cadastros.this, Transportadoras_Compras.class);
        startActivity(irTela);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Cadastros.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
