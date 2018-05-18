package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Compras extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras);
    }

    public void FornecedoresCompras(View v){
        Intent irTela = new Intent(Compras.this, Fornecedores_compras.class);
        startActivity(irTela);
    }

    public void ProdutosCompras(View v){
        Intent irTela = new Intent(Compras.this, Produtos_Compras.class);
        startActivity(irTela);
    }

    public void ServicosCompras(View v){
        Intent irTela = new Intent(Compras.this, Servicos_Compras.class);
        startActivity(irTela);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Compras.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
