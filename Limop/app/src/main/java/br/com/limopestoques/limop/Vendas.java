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

    public void ProdutosVendas(View v){
        Intent irTela = new Intent(Vendas.this, Produtos_Vendas.class);
        startActivity(irTela);
    }

    public void ServicosVendas(View v){
        Intent irTela = new Intent(Vendas.this, Servicos_Vendas.class);
        startActivity(irTela);
    }

    public void Parcelas(View v){
        Intent irTela = new Intent(Vendas.this, ParcelasVencer.class);
        startActivity(irTela);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Vendas.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }

}
